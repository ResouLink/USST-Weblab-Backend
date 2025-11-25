package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherDTO implements Serializable {
    /**
     * 性别，0是男，1是女
     */
    private long gender;
    /**
     * 老师介绍
     */
    private String intro;
    /**
     * 老师姓名
     */
    private String name;
    /**
     * 职称
     */
    private String title;
}
