package com.weblab.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherNotificationVO implements Serializable {
    /**
     * 通知内容
     */
    private String content;
    /**
     * 通知表主键
     */
    private long id;
    /**
     * 已读状态，0代表未读，1代表已读
     */
    private long status;
}
