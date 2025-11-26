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
public class CourseVO implements Serializable {
    /**
     * 开设学院
     */
    private String college;
    /**
     * 创建时间
     */
    private String createAt;
    /**
     * 课程描述
     */
    private String description;
    /**
     * 课程表主键
     */
    private long id;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 授课老师数组
     */
    private List<CourseTeacherVO> teachers;
    /**
     * 修改时间
     */
    private String updateAt;
}

