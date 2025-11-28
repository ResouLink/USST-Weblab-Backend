package com.weblab.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    /**
     * 开设学院
     */
    private String college;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
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
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateAt;
}
