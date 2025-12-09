package com.weblab.server.event;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.NotificationDao;
import com.weblab.server.dao.UserDao;
import com.weblab.server.entity.Notification;
import com.weblab.server.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NotificationListener implements ApplicationListener<NotificationEvent> {

    @Autowired
    private NotificationDao notificationDao;
    @Autowired
    private UserDao userDao;

    /**
     * 监听通知事件
     *
     * @param event
     */
    @Async
    @Override
    public void onApplicationEvent(NotificationEvent event) {
        List<Notification> notificationList = event.getNotificationList();
        NotificationType notificationType = event.getNotificationType();
        for (Notification notification : notificationList) {
            putNotificationQueue(notification.getId(), notificationType);
        }
    }

    public void putNotificationQueue(Long notificationId, NotificationType notificationType) {
        Notification notification = notificationDao.getById(notificationId);
        NotificationDto notificationDto = getNotificationDto(notification, notificationType);
        if (BeanUtil.isEmpty(notification) || BeanUtil.isEmpty(notificationDto.getUser())) {
            log.error("通知不存在");
            return;
        }
        boolean isPut = NotificationQueue.putNotification(notificationDto);
        if (!isPut) {
            log.error("插入队列被打断");
            throw new ServiceException("插入队列被打断");
        }
    }


    public NotificationDto getNotificationDto(Notification notification, NotificationType notificationType) {
        if (BeanUtil.isEmpty(notification) && notificationType != null) {
            NotificationDto notificationDto = new NotificationDto();
            // 获得通知用户
            if (notificationType == NotificationType.QUESTION) {
                // 获得老师
                Long teacherId = notification.getTeacherId();
                Users user = userDao.query().eq("role_id", teacherId).eq("user_role", 0).one();
                notificationDto.setNotification(notification);
                notificationDto.setUser(user.getId());
                return notificationDto;

            } else if (notificationType == NotificationType.ANSWER) {
                // 获得学生
                long studentId = notification.getStudentId();
                Users user = userDao.query().eq("role_id", studentId).eq("user_role", 1).one();
                notificationDto.setNotification(notification);
                notificationDto.setUser(user.getId());
                return notificationDto;
            }
        }
        return null;
    }
}
