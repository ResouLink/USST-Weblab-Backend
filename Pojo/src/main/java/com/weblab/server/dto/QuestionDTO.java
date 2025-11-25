package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionDTO implements Serializable {
    /**
     * 提问内容
     */
    private String content;
    /**
     * 问题所属课程
     */
    private long courseId;
    /**
     * 附件列表
     */
    private long[] files;
    /**
     * 提问者学生的主键
     */
    private long studentId;
}
