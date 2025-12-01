package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseDTO implements Serializable {
    /**
     * 开设学院
     */
    private String college;
    /**
     * 课程描述
     */
    private String description;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 授课老师主键数据
     */
    private List<Long> teachersId;
}
