package com.weblab.server.service;

import com.weblab.server.dto.StudentDTO;
import com.weblab.server.vo.StudentVO;

import java.util.List;

public interface StudentService {
    long addStudent(StudentDTO studentDTO);
    void updateStudent(StudentDTO studentDTO, long id);
    void deleteStudent(long id);
    StudentVO getStudentById(long id);
    List<StudentVO> getStudents(long page, long size, String keyword);
    void addStudentCourse(long studentId, long courseId);
    void deleteStudentCourse(long studentId, long courseId);
    List<Long> getStudentCourses(long studentId);
}
