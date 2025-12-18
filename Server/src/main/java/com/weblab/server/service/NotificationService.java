package com.weblab.server.service;

import com.weblab.server.entity.Notification;
import com.weblab.server.event.NotificationDto;
import com.weblab.server.event.NotificationType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface NotificationService {

    <T> List<Notification> addNotification(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException;
}
