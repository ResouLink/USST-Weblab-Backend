package com.weblab.server.service;


import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    ApiResult addStudent(StudentDTO studentDTO);
    ApiResult updateStudent(StudentDTO studentDTO, long id);
    ApiResult deleteStudent(long id);
    ApiResult getStudentById(long id);
    ApiResult getStudents(long page, long size, String keyword);
    void addStudentCourse(long studentId, long courseId);
    void deleteStudentCourse(long studentId, long courseId);
    List<Long> getStudentCourses(long studentId);
}
