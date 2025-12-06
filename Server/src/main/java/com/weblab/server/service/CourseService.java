package com.weblab.server.service;

import com.weblab.server.dto.CourseDTO;
import com.weblab.server.vo.CourseVO;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDTO courseDTO);
    void updateCourse(CourseDTO courseDTO, long id);
    void deleteCourse(long id);
    CourseVO getCourseById(long id);
    List<CourseVO> getCourses(long page, long size, String keyword);
}
