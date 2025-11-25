package com.weblab.server.vo;

import lombok.Data;

/**
 * 向教师端发送的提醒信息
 */
@Data
public class TMessageVo {
    private Long QuestionId; // 问题 id
    private String Text; // 提醒内容
}
