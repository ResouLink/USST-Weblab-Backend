package com.weblab.server.controller;

import com.weblab.server.event.SseClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    /**
     * SSE连接端点 - 由客户端主动调用
     */
    @GetMapping(value = "/connect")
    public SseEmitter connect(@RequestParam String userId) {
        // 这里可以添加额外的验证逻辑，比如验证token等
        return SseClient.connect(userId);
    }

    /**
     * 发送消息给指定用户
     */
    @PostMapping("/send/{userId}")
    public String sendMessage(
            @PathVariable String userId,
            @RequestBody MessageRequest request) {
        try {
            SseClient.sendMessage(userId, request.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "消息已发送";
    }

    /**
     * 断开连接
     */
    @GetMapping("/disconnect/{userId}")
    public String disconnect(@PathVariable String userId) {
        SseClient.removeUser(userId);
        return "连接已断开";
    }

    /**
     * 获取连接状态
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        return Map.of(
                "connectionCount", SseClient.getConnectionCount(),
                "timestamp", System.currentTimeMillis()
        );
    }

    @Data
    static class MessageRequest {
        private String content;
        private String sender;
        private Long timestamp;
    }
}