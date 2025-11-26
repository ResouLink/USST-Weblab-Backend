package com.weblab.server.service.impl;

import com.weblab.server.dao.CourseDao;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.service.CourseService;
import com.weblab.server.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private TeacherCourseDao teacherCourseDao;

    /**
     * 通过id查找课程
     * @param id
     * @return
     */
    @Override
    public CourseVO getById(Long id) {
        return courseDao.getById(id);
    }
}
