package com.weblab.server.service;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {
    ApiResult addTeacher(TeacherDTO teacherDTO);
    ApiResult updateTeacher(TeacherDTO teacherDTO, long id);
    ApiResult deleteTeacher(long id);
    ApiResult getTeacherById(long id);
    ApiResult getTeachers(long page, long size, String keyword);
    void addTeacherCourse(long teacherId, long courseId) throws Exception;
    void deleteTeacherCourse(long teacherId, long courseId) throws Exception;
    List<Long> getTeacherCourses(long teacherId);
}
