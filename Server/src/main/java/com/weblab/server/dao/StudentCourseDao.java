package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.StudentCourse;
import com.weblab.server.mapper.StudentCourseMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseDao extends ServiceImpl<StudentCourseMapper, StudentCourse> {
}
