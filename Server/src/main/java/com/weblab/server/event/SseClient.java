package com.weblab.server.event;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
@Component
public class SseClient {
    // 当前连接数
    private static final AtomicInteger count = new AtomicInteger(0);
    // 存储所有连接的sseEmitter
    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 发送心跳线程池
     */
    private static ScheduledExecutorService heartbeatExecutors = Executors.newScheduledThreadPool(8);

    public static SseEmitter connect(String userId) {
        SseEmitter haveSseEmitter = sseEmitterMap.get(userId);
        if (haveSseEmitter != null) {
//             已存在则返回
            log.info("用户{}已存在连接", userId);
            try {
                haveSseEmitter.send(SseEmitter.event().comment(""));
                log.info("用户{}已连接成功", userId);
                return haveSseEmitter;
            } catch (IOException e) {
                log.error("连接已失效", e);
                removeUser(userId);
            }
        }
        SseEmitter sseEmitter = new SseEmitter(0L);
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(1000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitterMap.put(userId, sseEmitter);
        // 数量+1
        count.getAndIncrement();

        final ScheduledFuture<?> future = heartbeatExecutors.scheduleAtFixedRate(new HeartBeatTask(userId), 0, 1, TimeUnit.SECONDS);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId,  future));
        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeoutCallBack(userId));

        log.info("用户{}连接成功，当前连接数：{}", userId, count.get());
        return sseEmitter;
    }

    /**
     * 给指定用户发送消息
     *
     * @param userId
     * @param message
     * @return
     */
    public static Boolean sendMessage(String userId, String message) throws IOException {
        SseEmitter sseEmitter = sseEmitterMap.get(userId);
        if (sseEmitter == null) {
            log.info("消息推送失败userId:[{}],没有创建连接!", userId);
            return false;
        }

        String eventId = userId + "-" + System.currentTimeMillis();
        sseEmitter.send(SseEmitter.event()
                .id(eventId)
                .reconnectTime(3 * 1000L)
                .data(message));
        return true;

    }


    private static Runnable completionCallBack(String userId, ScheduledFuture<?>  future) {
        return () -> {
            log.info("结束 sse 连接：{}", userId);
            removeUser(userId);
            if (future != null) {
                future.cancel(true);
            }
        };
    }

    private static Runnable timeoutCallBack(String userId) {
        return () -> {
            log.info("连接 sse 连接超时：{}", userId);
            removeUser(userId);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> {
            log.info("[{}]连接异常,{}", userId, throwable.toString());
            SseEmitter sseEmitter = sseEmitterMap.get(userId);
            if (sseEmitter != null) {
                try {
                    String errorMessage = "连接发生异常，请重试！";
                    sseEmitter.send(SseEmitter.event()
                            .name("error")
                            .data(errorMessage));
                } catch (IOException e) {
                    log.error("发送错误消息失败", e);
                } finally {
                    // 确保资源被正确释放
                    try {
                        sseEmitter.complete();
                    } catch (Exception e) {
                        log.debug("SSE emitter completion failed", e);
                    }
                }
            }
            removeUser(userId);
        };
    }


    public static void removeUser(String userId) {
        SseEmitter emitter = sseEmitterMap.remove(userId);
        if (emitter != null) {
            try {
                emitter.complete();
            } catch (Exception e) {
                log.debug("用户{}连接完成异常: {}", userId, e.getMessage());
            }

            // 更新计数器
            int current = count.decrementAndGet();
            if (current < 0) {
                count.set(0); // 防止负数
                log.warn("连接计数器出现负数，已重置为0");
            }
        }
    }

    public static boolean hasConnection(String userId) {
        return sseEmitterMap.containsKey(userId);
    }

    // 获取连接数
    public static int getConnectionCount() {
        return count.get();
    }
}
