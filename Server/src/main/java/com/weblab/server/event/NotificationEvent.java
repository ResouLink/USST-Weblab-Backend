package com.weblab.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.time.Clock;


public class NotificationEvent extends ApplicationEvent {
    private Long notificationId; // 通知id

    public NotificationEvent(Object source, Long notificationId) {
        super(source);
        this.notificationId = notificationId;
    }

    public NotificationEvent(Object source, Clock clock, Long notificationId) {
        super(source, clock);
        this.notificationId = notificationId;
    }

    public Long getNotificationId() {
        return notificationId;
    }
}
