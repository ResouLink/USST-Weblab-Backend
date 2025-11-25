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
public class UserVO implements Serializable {
    /**
     * 头像url
     */
    private String avatarUrl;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 用户表主键
     */
    private long id;
    /**
     * 对应角色主键，先通过role去判断是谁然后再去对应的表中找
     */
    private String roleId;
    /**
     * 用户角色，0代表老师，1代表学生
     */
    private long userRole;
    /**
     * 用户名称
     */
    private String username;
}
