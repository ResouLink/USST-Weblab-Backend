package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.User;
import com.weblab.server.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserDao extends ServiceImpl<UserMapper, User> {


}
