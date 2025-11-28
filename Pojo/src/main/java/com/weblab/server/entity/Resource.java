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
public class Resource implements Serializable {
    /**
     * 学习资源表主键
     */
    private long id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 课程表主键，说明这个学习资源属于哪个课程里的
     */
    private long courseId;

    /**
     * 资源描述
     */
    private String description;
    /**
     * 下载次数
     */
    private long downloadCount;
    /**
     * 上传者主键，取决于上传者角色是谁去哪个表照查
     */
    private long uploaderId;
    /**
     * 上传者角色，0是老师，1是学生
     */
    private long uploaderRole;
    /**
     * 可见性，0代表只对本班级开放，1代表对所有人开放，2表示只对上传者可见，3表示管理员隐藏
     */
    private long visibility;
    /**
     * 初次上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createAt;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateAt;
}
