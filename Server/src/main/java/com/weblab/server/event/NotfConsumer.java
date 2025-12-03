package com.weblab.server.event;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.weblab.common.utils.SecurityUtil;
import com.weblab.server.entity.Notification;
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
                String content = JSONUtil.toJsonStr(consumeNotification);
                // todo
                String loginUser = StrUtil.toString(SecurityUtil.getUserId());
                Boolean isSend = SseClient.sendMessage(loginUser, content);
                if (!isSend) {
                    log.info("消息推送失败，将消息重新放入队列重试！");
                    NotificationQueue.putNotification(consumeNotification);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
