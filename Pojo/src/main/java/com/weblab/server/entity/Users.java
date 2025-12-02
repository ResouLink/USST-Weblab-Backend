package com.weblab.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {
    /**
     * 用户表主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户密码，可以选择加密，但省事的做法就是不要做加密
     */
    private String password;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 对应角色主键，先通过role去判断是谁然后再去对应的表中找
     */
    private Long roleId;
    /**
     * 用户角色，0代表老师，1代表学生
     */
    private Long userRole;

}
