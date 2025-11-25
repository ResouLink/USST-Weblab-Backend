package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherNotificationDTO implements Serializable {
    /**
     * 通知内容
     */
    private String content;
}
