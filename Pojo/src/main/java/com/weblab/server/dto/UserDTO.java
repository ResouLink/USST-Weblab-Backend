package com.weblab.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    /**
     * 头像在附件表中的主键
     */
    private long avatarId;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 用户密码，可以选择加密，但省事的做法就是不要做加密
     */
    private String password;
    /**
     * 用户角色，0代表老师，1代表学生
     */
    private long userRole;
    /**
     * 用户名称
     */
    private String username;
}
