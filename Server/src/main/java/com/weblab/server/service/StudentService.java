package com.weblab.server.service;


import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.dto.StudentDTO;
import com.weblab.server.vo.StudentVO;

public interface StudentService {
    ApiResult addStudent(StudentDTO studentDTO);
    ApiResult updateStudent(StudentDTO studentDTO, long id);
    ApiResult deleteStudent(long id);
    ApiResult getStudentById(long id);
    ApiResult getStudents(long page, long size, String keyword);
}
