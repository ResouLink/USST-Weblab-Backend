package com.weblab.server.event;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
@Component
public class SseClient {
    // 当前连接数
    private static final AtomicInteger count = new AtomicInteger(0);
    // 存储所有连接的sseEmitter
    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId) {
//        if (!BeanUtil.isEmpty(sseEmitterMap.get(userId))) {
//             已存在则返回
//            log.info("用户{}已存在连接", userId);
//            return sseEmitterMap.get(userId);
//        }
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId));
        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeoutCallBack(userId));

        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitterMap.put(userId, sseEmitter);
        // 数量+1
        count.getAndIncrement();
        log.info("用户{}连接成功，当前连接数：{}", userId, count.get());
        return sseEmitter;
    }

    /**
     * 给指定用户发送消息
     * @param userId
     * @param message
     * @return
     */
    public static Boolean sendMessage(String userId, String message) {
        SseEmitter sseEmitter = sseEmitterMap.get(userId);
        if (sseEmitter == null) {
            log.info("消息推送失败userId:[{}],没有创建连接!", userId);
            return true;
        }
        try {
            String eventId = userId + "-" + System.currentTimeMillis();
            sseEmitter.send(SseEmitter.event()
                    .id(eventId)
                    .reconnectTime(3*1000L)
                    .data(message));
            return true;
        } catch (IOException e) {
            removeUser(userId);
            log.error("发送消息失败", e);
            return false;
        }
    }


    private static Runnable completionCallBack(String userId) {
        return () -> {
            log.info("结束 sse 连接：{}", userId);
            removeUser(userId);
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
            log.info("sse 连接异常：{}", userId);
            removeUser(userId);
        };
    }


    public static void removeUser(String userId) {
        SseEmitter emitter = sseEmitterMap.get(userId);
        if (emitter != null) {
            try {
                emitter.complete();
                // 数量-1
                count.getAndDecrement();
                log.info("用户{}连接已关闭，当前连接数：{}", userId, count.get());
            } catch (IllegalStateException e) {
                // emitter可能已经完成或超时
                log.warn("用户{}的SSE连接已完成或超时: {}", userId, e.getMessage());
                // 仍然需要递减计数器，因为用户已被移除
                count.getAndDecrement();
                log.info("用户{}连接已关闭，当前连接数：{}", userId, count.get());
            } catch (Exception e) {
                log.error("关闭用户{}的SSE连接时发生错误", userId, e);
                // 仍然需要递减计数器，因为用户已被移除
                count.getAndDecrement();
                log.info("用户{}连接已关闭，当前连接数：{}", userId, count.get());
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
