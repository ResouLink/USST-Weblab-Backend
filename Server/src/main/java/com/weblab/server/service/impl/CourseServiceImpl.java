package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.result.ApiResult;
import com.weblab.server.dao.CourseDao;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.dao.TeacherDao;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.entity.Course;
import com.weblab.server.entity.TeacherCourse;
import com.weblab.server.service.CourseService;
import com.weblab.server.vo.CourseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;
    private final TeacherCourseDao teacherCourseDao;
    private final TeacherDao teacherDao;

    @Override
    public ApiResult addCourse(CourseDTO courseDTO) {
        Course newCourse = new Course();
        BeanUtils.copyProperties(courseDTO, newCourse);
        courseDao.save(newCourse);
        
        // 添加教师关系
        if (courseDTO.getTeachersId() != null && courseDTO.getTeachersId().length > 0) {
            for (Long teacherId : courseDTO.getTeachersId()) {
                if (teacherDao.getById(teacherId) == null) {
                    log.warn("教师不存在, ID: {}", teacherId);
                    return ApiResult.fail(1, "教师ID不存在: " + teacherId);
                }
                TeacherCourse teacherCourse = new TeacherCourse();
                teacherCourse.setCourseId(newCourse.getId());
                teacherCourse.setTeacherId(teacherId);
                teacherCourseDao.save(teacherCourse);
            }
        }
        log.info("课程添加成功");
        return ApiResult.success("添加课程成功");
    }

    @Override
    public ApiResult updateCourse(CourseDTO courseDTO, long id) {
        Course existing = courseDao.getById(id);
        if (existing == null) {
            log.warn("课程不存在");
            return ApiResult.fail(1, "课程不存在");
        }

        BeanUtils.copyProperties(courseDTO, existing);
        existing.setId(id);

        boolean updated = courseDao.updateById(existing);
        if (updated) {
            // 更新教师关系
            if (courseDTO.getTeachersId() != null && courseDTO.getTeachersId().length > 0) {
                teacherCourseDao.remove(new QueryWrapper<TeacherCourse>().eq("course_id", id));
                for (Long teacherId : courseDTO.getTeachersId()) {
                    if (teacherDao.getById(teacherId) == null) {
                        log.warn("教师不存在, ID: {}", teacherId);
                        return ApiResult.fail(1, "教师ID不存在: " + teacherId);
                    }
                    TeacherCourse teacherCourse = new TeacherCourse();
                    teacherCourse.setCourseId(id);
                    teacherCourse.setTeacherId(teacherId);
                    teacherCourseDao.save(teacherCourse);
                }
            }
            log.info("课程更新成功");
            return ApiResult.success("课程更新成功", 1);
        } else {
            log.warn("课程更新失败");
            return ApiResult.fail(1, "更新失败");
        }
    }

    @Override
    public ApiResult deleteCourse(long id) {
        boolean removed = courseDao.removeById(id);
        if (removed) {
            teacherCourseDao.remove(new QueryWrapper<TeacherCourse>().eq("course_id", id));
            log.info("课程删除成功");
            return ApiResult.success("课程删除成功");
        } else {
            log.warn("课程删除失败");
            return ApiResult.fail("删除失败，课程不存在");
        }
    }

    @Override
    public ApiResult getCourseById(long id) {
        Course course = courseDao.getById(id);
        if (course == null) {
            log.warn("课程不存在, ID: {}", id);
            return ApiResult.fail("课程不存在");
        }
        CourseVO vo = new CourseVO();
        BeanUtils.copyProperties(course, vo);
        return ApiResult.success(vo);
    }

    @Override
    public ApiResult getCourses(long page, long size, String keyword) {
        Page<Course> pageParam = new Page<>(page, size);

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword);
        }

        Page<Course> resultPage = courseDao.page(pageParam, queryWrapper);

        List<CourseVO> voList = resultPage.getRecords().stream().map(course -> {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(course, vo);
            return vo;
        }).collect(Collectors.toList());
        
        return ApiResult.success(voList);
    }
}
