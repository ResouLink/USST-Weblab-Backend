package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerDTO implements Serializable {
    /**
     * 回答内容，文本内容
     */
    private String content;
    /**
     * 上传文件列表
     */
    private long[] files;
    /**
     * 老师表主键，回答问题的老师
     */
    private long teacherId;
}
