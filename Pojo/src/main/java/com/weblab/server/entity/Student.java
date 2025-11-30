package com.weblab.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Student implements Serializable {
    /**
     * 学生表主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
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
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;
    /**
     * 创建时间，注册账号后第一次保存个人资料
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt;
}
