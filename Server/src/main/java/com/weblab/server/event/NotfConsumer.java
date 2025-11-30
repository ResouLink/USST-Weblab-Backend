package com.weblab.server.event;

import cn.hutool.core.util.StrUtil;
import com.weblab.server.entity.Notification;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class NotfConsumer implements Runnable {
    @Autowired
    @Qualifier("notificationExecutor")
    private ExecutorService notificationExecutor;
    @Autowired
    private SseClient sseClient;

    @PostConstruct
    public void init(){
        notificationExecutor.submit(this);
    }

    public Notification consume() throws InterruptedException {
        return NotificationQueue.takeNotification();
    }

    @Override
    public void run() {
        while(true){
            try {
                Notification consumeNotification = consume();
                if (consumeNotification == null) {
                    continue;
                }
                // todo 发送通知

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
