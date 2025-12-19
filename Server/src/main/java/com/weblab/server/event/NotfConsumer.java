package com.weblab.server.event;

import cn.hutool.core.util.StrUtil;
import com.weblab.server.dao.NotificationDao;
import com.weblab.server.entity.Notification;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class NotfConsumer implements Runnable, DisposableBean {

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    @Qualifier("notificationExecutor")
    private ExecutorService notificationExecutor;

    private volatile boolean running = true;


    @PostConstruct
    public void init() {
        notificationExecutor.submit(this);
        log.info("启动通知消费者线程！");
    }

    public NotificationDto consume() throws InterruptedException {
        return NotificationQueue.takeNotification();
    }

    @Override
    public void run() {
        while (running) {
            try {
                log.info("开始消费通知！");
                NotificationDto notificationDto = consume();
                if (notificationDto == null) {
                    continue;
                }
                Boolean isSend = SseClient.sendMessage(StrUtil.toString(notificationDto.getUser()), notificationDto.getNotification().getContent());
                if (!isSend) {
                    log.info("消息推送失败，将消息重新放入队列重试！");
                    retry(notificationDto);
                }
                // 通知成功， 设置通知为已通知
                Notification notification = notificationDto.getNotification();
                notification.setStatus(1);
                notificationDao.updateById(notification);
                log.info("消息推送成功！");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void retry(NotificationDto notificationDto) {
        int retryCount = 3;
        while (retryCount > 0) {
            log.info("消息推送失败，将消息重新放入队列重试！");
            boolean isTrue = NotificationQueue.putNotification(notificationDto);
            if (isTrue) {
                log.info("消息推送成功！");
                return;
            }
            retryCount--;
        }
        log.error("通知已达最大重试次数，丢弃该通知，用户={}, 内容={}",
                notificationDto.getUser(),
                notificationDto.getNotification().getContent());

    }

    @Override
    public void destroy() throws Exception {
        this.running = false;
        notificationExecutor.shutdownNow();
        log.info("通知消费者服务已关闭");
    }
}
