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
        if (!BeanUtil.isEmpty(sseEmitterMap.get(userId))) {
            // 已存在则返回
            return sseEmitterMap.get(userId);
        }
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId));
        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeoutCallBack(userId));
        sseEmitterMap.put(userId, sseEmitter);
        // 数量+1
        count.getAndIncrement();
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
            log.info("消息推送失败userId:[{}],没有创建连接，请重试。", userId);
            return false;
        }
        try {
            String eventId = userId + "-" + System.currentTimeMillis();
            sseEmitter.send(SseEmitter.event()
                    .id(eventId)
                    .reconnectTime(3*1000L)
                    .data(message), MediaType.APPLICATION_JSON);
            return true;
        } catch (IOException e) {
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
                    sseEmitter.send(SseEmitter.event()
                            .name("error")
                            .data("连接发生异常: " + throwable.getMessage()) + "，请重试！");
                } catch (IOException e) {
                    log.error("发送错误消息失败", e);
                }
            }
            log.info("sse 连接异常：{}", userId);
            removeUser(userId);
        };
    }


    public static void removeUser(String userId) {
        SseEmitter emitter = sseEmitterMap.get(userId);
        if (emitter != null) {
            emitter.complete();
        }
        sseEmitterMap.remove(userId);
        // 数量-1
        count.getAndDecrement();
    }
}
