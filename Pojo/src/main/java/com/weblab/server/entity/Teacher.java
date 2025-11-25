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
public class Teacher implements Serializable {
    /**
     * 老师表主键
     */
    private long id;
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
    /**
     * 修改时间
     */
    private String updateAt;
    /**
     * 创建时间
     */
    private String createAt;
}
