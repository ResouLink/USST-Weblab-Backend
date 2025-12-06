package com.weblab.server.service;

import com.weblab.server.dto.TeacherDTO;
import com.weblab.server.vo.TeacherVO;

import java.util.List;

public interface TeacherService {
    long addTeacher(TeacherDTO teacherDTO);
    void updateTeacher(TeacherDTO teacherDTO, long id);
    void deleteTeacher(long id);
    TeacherVO getTeacherById(long id);
    List<TeacherVO> getTeachers(long page, long size, String keyword);
    void addTeacherCourse(long teacherId, long courseId) throws Exception;
    void deleteTeacherCourse(long teacherId, long courseId) throws Exception;
    List<Long> getTeacherCourses(long teacherId);
}
