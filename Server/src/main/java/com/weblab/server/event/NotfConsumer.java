package com.weblab.server.event;

import com.weblab.server.entity.Notification;
import com.weblab.server.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class NotfConsumer implements Runnable {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("notificationExecutor")
    private ExecutorService notificationExecutor;
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
                // todo 获得发送用户

//                userService.getUserByRoleId(consumeNotification.getUserRole(), consumeNotification.getStudentId());
//                Boolean isSend = SseClient.sendMessage(loginUser, content);
//                if (!isSend) {
//                    log.info("消息推送失败，将消息重新放入队列重试！");
//                    NotificationQueue.putNotification(consumeNotification);
//                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
