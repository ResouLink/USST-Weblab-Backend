package com.weblab.server.service;

import com.weblab.server.entity.Notification;
import com.weblab.server.event.NotificationDto;
import com.weblab.server.event.NotificationType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface NotificationService {

    <T> List<Notification> addNotification(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException;

    /**
     * 获取用户未读通知数量
     */
    long getUnreadCount(Long userId);

    /**
     * 获取用户的通知列表
     */
    List<Notification> getNotificationsByUserId(Long userId, Integer status);

    /**
     * 标记单条通知为已读
     */
    void markAsRead(Long notificationId);

    /**
     * 标记用户的所有通知为已读
     */
    void markAllAsRead(Long userId);
}
