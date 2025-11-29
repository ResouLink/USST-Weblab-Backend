package com.weblab.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weblab.server.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
