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
public class Answer implements Serializable {
    /**
     * 回答问题表主键
     */
    private long id;
    /**
     * 回答内容，文本内容
     */
    private String content;

    /**
     * 老师表主键，回答问题的老师
     */
    private long teacherId;
}
