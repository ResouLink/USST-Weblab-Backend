package com.weblab.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentVO implements Serializable {
    /**
     * 所属学院
     */
    private String college;
    /**
     * 创建时间，注册账号后第一次保存个人资料
     */
    private LocalDateTime createAt;
    /**
     * 性别，0是男，1是女
     */
    private long gender;
    /**
     * 学生表主键
     */
    private long id;
    /**
     * 专业名称
     */
    private String major;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 修改时间
     */
    private LocalDateTime updateAt;
}


