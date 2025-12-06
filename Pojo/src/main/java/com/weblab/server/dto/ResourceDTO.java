package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ResourceDTO implements Serializable {
    /**
     * 课程表主键，说明这个学习资源属于哪个课程里的
     */
    private long courseId;
    /**
     * 资源描述
     */
    private String description;
    /**
     * 上传文件列表，是oss接口返回的在附件表中的主键数组
     */
    private List<Long> files;
    /**
     * 资源名称
     */
    private String name;
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
}
