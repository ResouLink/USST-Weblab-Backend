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
public class Question implements Serializable {
    /**
     * 问题表主键
     */
    private long id;
    /**
     * 提问内容
     */
    private String content;
    /**
     * 问题所属课程
     */
    private long courseId;

    /**
     * 是否回答过，0是没有，1是有
     */
    private long isAnswered;
    /**
     * 提问者学生的主键
     */
    private long studentId;
}
