package com.weblab.server.event;

import com.weblab.server.entity.Notification;

import java.util.List;

public class NotificationDto {
    private Notification notification;
    private List<Long> userList; // 接收通知的用户ID列表
}
