package com.weblab.server.event;

import com.weblab.server.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NotificationDto {
    private Notification notification;
    private Long user; // 接收通知的用户ID
}
