package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.Student;
import com.weblab.server.mapper.StudentMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentDao extends ServiceImpl<StudentMapper, Student> {
}
