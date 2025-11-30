package com.weblab.server.event;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.NotificationDao;
import com.weblab.server.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener implements ApplicationListener<NotificationEvent> {

    @Autowired
    private NotificationDao notificationDao;

    /**
     * 监听通知事件
     * @param event
     */
    @Override
    public void onApplicationEvent(NotificationEvent event) {
        Long notificationId = event.getNotificationId();
        putNotificationQueue(notificationId);
    }

    public void putNotificationQueue(Long notificationId) {
        Notification notification = notificationDao.getById(notificationId);
        if (BeanUtil.isEmpty(notification)) {
            throw new ServiceException("通知不存在");
        }
        boolean isPut = NotificationQueue.putNotification(notification);
        if (!isPut) {
            throw new ServiceException("插入队列被打断");
        }
    }
}
