package com.weblab.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.CourseDao;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.dao.TeacherDao;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.dto.PageDto;
import com.weblab.server.entity.Course;
import com.weblab.server.entity.TeacherCourse;
import com.weblab.server.service.CourseService;
import com.weblab.server.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private TeacherCourseDao teacherCourseDao;
    @Autowired
    private TeacherDao teacherDao;

    /**
     * 通过id查找课程
     *
     * @param id
     * @return
     */
    @Override
    public CourseVO getById(Long id) {
        return courseDao.getById(id);
    }

    /**
     * 新增课程
     *
     * @param courseDTO
     * @return
     */
    @Override
    public Boolean save(CourseDTO courseDTO) {
        if (BeanUtil.isEmpty(courseDTO)) {
            throw new ServiceException("新增课程为空!");
        }
        Course course = new Course();
        BeanUtil.copyProperties(courseDTO, course); // 确保属性名一致
        if (courseDTO.getTeachersId().length == 0) {
            courseDao.save(course);
            return true;
        }
        // 检查老师id是否存在
        long[] teachersId = courseDTO.getTeachersId();
        for (Long teacherId : teachersId) {
            // 检查教师是否存在
            if (teacherDao.getById(teacherId) == null) {
                // 可以选择抛出异常或记录日志
                throw new ServiceException("教师ID不存在: " + teacherId);
            }
        }
        for (Long teacherId : teachersId) {
            TeacherCourse teacherCourse = TeacherCourse.builder().courseId(course.getId()).teacherId(teacherId).build();
            teacherCourseDao.save(teacherCourse);
        }
        return true;
    }

    /**
     * 删除课程
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delete(Long id) {
        if (BeanUtil.isEmpty(id)) {
            throw new ServiceException("删除id为空!");
        }
        courseDao.removeById(id);
        return false;
    }

    /**
     * 修改课程
     *
     * @param courseDTO
     * @return
     */
    @Override
    public Boolean update(CourseDTO courseDTO) {
        if (BeanUtil.isEmpty(courseDTO)) {
            throw new ServiceException("修改课程内容为空!");
        }
        Course course = new Course();
        BeanUtil.copyProperties(courseDTO, course);
        if (courseDTO.getTeachersId().length == 0) {
            courseDao.updateById(course);
            return true;
        }
        // 检查老师id是否存在
        long[] teachersId = courseDTO.getTeachersId();
        for (Long teacherId : teachersId) {
            // 检查教师是否存在
            if (teacherDao.getById(teacherId) == null) {
                // 可以选择抛出异常或记录日志
                throw new ServiceException("教师ID不存在: " + teacherId);
            }
        }
        for (Long teacherId : teachersId) {
            TeacherCourse teacherCourse = TeacherCourse.builder().courseId(course.getId()).teacherId(teacherId).build();
            teacherCourseDao.updateById(teacherCourse);
        }
        return true;
    }

    /**
     * 获取课程列表(分页)
     *
     * @param pageDto
     * @return
     */
    @Override
    public List<CourseVO> list(PageDto pageDto) {
        return courseDao.list(pageDto);
    }
}
