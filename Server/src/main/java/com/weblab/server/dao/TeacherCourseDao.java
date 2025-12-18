package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.TeacherCourse;
import com.weblab.server.mapper.TeacherCourseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherCourseDao extends ServiceImpl<TeacherCourseMapper, TeacherCourse> {
    public List<Long> getByCourseId(long courseId){
        return query().eq("course_id", courseId).list().stream().map(TeacherCourse::getTeacherId).toList();
    }
}
