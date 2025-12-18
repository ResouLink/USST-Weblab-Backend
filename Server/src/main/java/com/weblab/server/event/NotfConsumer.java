package com.weblab.server.event;

import cn.hutool.core.util.StrUtil;
import com.weblab.server.dao.NotificationDao;
import com.weblab.server.dao.UserDao;
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
    private NotificationDao notificationDao;

    @Autowired
    @Qualifier("notificationExecutor")
    private ExecutorService notificationExecutor;


    @PostConstruct
    public void init() {
        notificationExecutor.submit(this);
    }

    public NotificationDto consume() throws InterruptedException {
        return NotificationQueue.takeNotification();
    }

    @Override
    public void run() {
        while (true) {
            try {
                NotificationDto consumeNotification = consume();
                if (consumeNotification == null) {
                    continue;
                }
                Boolean isSend = SseClient.sendMessage(StrUtil.toString(consumeNotification.getUser()), consumeNotification.getNotification().getContent());
                if (!isSend) {
                    log.info("消息推送失败，将消息重新放入队列重试！");
                    NotificationQueue.putNotification(consumeNotification);
                }
                // 通知成功， 设置通知为已通知
                Notification notification = consumeNotification.getNotification();
                notification.setStatus(1);
                notificationDao.updateById(notification);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
