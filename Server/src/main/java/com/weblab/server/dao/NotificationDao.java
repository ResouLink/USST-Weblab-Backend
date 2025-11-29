package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Notification;
import com.weblab.server.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

@Service
public class NotificationDao extends ServiceImpl<NotificationMapper, Notification> {
}
