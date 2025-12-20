package com.weblab.server.event;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class HeartBeatTask implements Runnable {

    private final String clientId;

    public HeartBeatTask(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void run() {
        log.info("发送心跳包给客户端：{}", clientId);
        try {
            SseClient.sendMessage(clientId, "ping");
        } catch (IOException e) {
            log.error("发送心跳包失败", e);
        }
    }
}
