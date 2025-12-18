package com.weblab.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


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
    private List<String> files;
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

    /**
     * 问题的回答列表
     */
    private List<Long> answerIds;
    /**
     * 提问者学生的姓名
     */
    private String studentName;
    /**
     * 问题所属于的课程名字
     */
    private String courseName;
}
