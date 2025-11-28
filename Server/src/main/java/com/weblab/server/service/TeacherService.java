package com.weblab.server.service;

import com.weblab.server.dto.PageDto;
import com.weblab.server.entity.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher getById(Long id);

    List<Teacher> list(PageDto pageDto);

    Boolean save(Teacher teacher);

    Boolean update(Teacher teacher);

    Boolean delete(Long id);

    Boolean addTeachCourse(Long teacherId, Long courseId);

    Boolean deleteTeachCourse(Long teacherId, Long courseId);

    List<Long> getTeachCourses(Long id);
}
