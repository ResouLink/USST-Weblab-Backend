package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class StudentDTO implements Serializable {
    /**
     * 所属学院
     */
    private String college;
    /**
     * 性别，0是男，1是女
     */
    private long gender;
    /**
     * 专业名称
     */
    private String major;
    /**
     * 学生姓名
     */
    private String name;
}
