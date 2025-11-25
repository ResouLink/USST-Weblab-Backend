package com.weblab.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {
    /**
    * 通知表主键
    */
    private long id;
    /**
     * 通知内容
     */
    private String content;

    /**
     * 已读状态，0代表未读，1代表已读
     */
    private long status;
    /**
     * 通知接送者学生的主键
     */
    private long studentId;
    /**
     * 通知发送者老师的主键
     */
    private long teacherId;
}
