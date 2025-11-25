package com.weblab.server.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceVO implements Serializable {
    /**
     * 课程表主键，说明这个学习资源属于哪个课程里的
     */
    private long courseId;
    /**
     * 初次上传时间
     */
    private String createAt;
    /**
     * 资源描述
     */
    private String description;
    /**
     * 下载次数
     */
    private long downloadCount;
    /**
     * 资源所在路径
     */
    private String fileUrl;
    /**
     * 学习资源表主键
     */
    private long id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 修改时间
     */
    private String updateAt;
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
