package com.weblab.server.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Users;
import com.weblab.server.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserDao extends ServiceImpl<UserMapper, Users> {
    public Users getByUsername(String username) {
        return this.getOne(
                new LambdaQueryWrapper<Users>()
                        .eq(Users::getUsername, username)
        );
    }
}
