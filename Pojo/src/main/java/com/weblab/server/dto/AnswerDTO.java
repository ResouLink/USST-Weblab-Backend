package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AnswerDTO implements Serializable {
    /**
     * 回答内容，文本内容
     */
    private String content;
    /**
     * 上传文件列表
     */
    private List<Long> files;
    /**
     * 老师表主键，回答问题的老师
     */
    private long teacherId;
    /**
     * 回答的问题的主键
     */
    private Long questionId;
}
