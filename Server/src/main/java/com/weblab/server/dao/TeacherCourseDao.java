package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.TeacherCourse;
import com.weblab.server.mapper.TeacherCourseMapper;
import org.springframework.stereotype.Service;

@Service
public class TeacherCourseDao extends ServiceImpl<TeacherCourseMapper, TeacherCourse> {
}
