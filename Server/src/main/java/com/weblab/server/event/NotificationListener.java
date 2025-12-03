package com.weblab.server.event;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.NotificationDao;
import com.weblab.server.entity.Notification;
import com.weblab.server.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener implements ApplicationListener<NotificationEvent> {

    @Autowired
    private NotificationDao notificationDao;

    /**
     * 监听通知事件
     * @param event
     */
    @Async
    @Override
    public void onApplicationEvent(NotificationEvent event) {
        Long notificationId = event.getNotificationId();
        putNotificationQueue(notificationId);
    }

    public void putNotificationQueue(Long notificationId) {
        Notification notification = notificationDao.getById(notificationId);
        if (BeanUtil.isEmpty(notification)) {
            log.error("通知不存在");
            return;
        }
        boolean isPut = NotificationQueue.putNotification(notification);
        if (!isPut) {
            log.error("插入队列被打断");
            throw new ServiceException("插入队列被打断");
        }
    }

    // todo
    Users getAcceptUser(Notification notification){
        return null;
    }
}
