package com.weblab.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weblab.server.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserMapper extends BaseMapper<User> {
}
