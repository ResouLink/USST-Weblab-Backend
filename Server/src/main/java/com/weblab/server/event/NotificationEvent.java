package com.weblab.server.event;

import com.weblab.server.entity.Notification;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.List;


public class NotificationEvent extends ApplicationEvent {
    private List<Notification> notificationList; // 通知id
    private NotificationType notificationType; // 通知类型

    public NotificationEvent(Object source, List<Notification> notificationList, NotificationType notificationType) {
        super(source);
        this.notificationList = notificationList;
        this.notificationType = notificationType;
    }

    public NotificationEvent(Object source, Clock clock, List<Notification> notificationList, NotificationType notificationType) {
        super(source, clock);
        this.notificationList = notificationList;
        this.notificationType = notificationType;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }
    public NotificationType getNotificationType() {
        return notificationType;
    }
}
