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
public class AnswerVO implements Serializable {
    /**
     * 回答内容，文本内容
     */
    private String content;
    /**
     * 附件文件列表
     */
    private List<String> files;
    /**
     * 回答问题表主键
     */
    private long id;
    /**
     * 老师表主键，回答问题的老师
     */
    private long teacherId;
    /**
     * 老师姓名
     */
    private String teacherName;
}
