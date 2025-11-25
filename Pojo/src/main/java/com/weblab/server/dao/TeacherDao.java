package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Teacher;
import com.weblab.server.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

@Service
public class TeacherDao extends ServiceImpl<TeacherMapper, Teacher> {
}
