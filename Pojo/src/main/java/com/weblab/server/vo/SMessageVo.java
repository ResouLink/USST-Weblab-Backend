package com.weblab.server.vo;

import lombok.Data;

/**
 * 提醒学生的消息
 */
@Data
public class SMessageVo {
    private Long AnswerId; // 回答 id
    private Long QuestionId; // 问题 id
    private String Text; // 提醒内容
}
