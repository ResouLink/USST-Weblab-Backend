package com.weblab.server.service;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.CourseDTO;

public interface CourseService {
    ApiResult addCourse(CourseDTO courseDTO);
    ApiResult updateCourse(CourseDTO courseDTO, long id);
    ApiResult deleteCourse(long id);
    ApiResult getCourseById(long id);
    ApiResult getCourses(long page, long size, String keyword);
}
