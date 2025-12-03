package com.weblab.server.service;

import com.weblab.server.entity.Notification;

import java.lang.reflect.InvocationTargetException;

public interface NotificationService {

    <T, D> Notification addNotification(T t, D d) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException;
}
