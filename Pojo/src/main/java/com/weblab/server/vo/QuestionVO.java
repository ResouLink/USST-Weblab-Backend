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
public class QuestionVO implements Serializable {
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
    private String[] files;
    /**
     * 问题表主键
     */
    private long id;
    /**
     * 是否回答过，0是没有，1是有
     */
    private long isAnswered;
    /**
     * 提问者学生的主键
     */
    private long studentId;
}
