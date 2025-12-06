package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.server.dao.CourseDao;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.dao.TeacherDao;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.entity.Course;
import com.weblab.server.entity.Teacher;
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
    public void addCourse(CourseDTO courseDTO) {
        Course newCourse = new Course();
        BeanUtils.copyProperties(courseDTO, newCourse);
        courseDao.save(newCourse);
        
        // 添加教师关系
        if (courseDTO.getTeachersId() != null && !courseDTO.getTeachersId().isEmpty()) {
            for (Long teacherId : courseDTO.getTeachersId()) {
                if (teacherDao.getById(teacherId) == null) {
                    log.warn("教师不存在, ID: {}", teacherId);
                    throw new RuntimeException("教师ID不存在: " + teacherId);
                }
                TeacherCourse teacherCourse = new TeacherCourse();
                teacherCourse.setCourseId(newCourse.getId());
                teacherCourse.setTeacherId(teacherId);
                teacherCourseDao.save(teacherCourse);
            }
        }
        log.info("课程添加成功");
    }

    @Override
    public void updateCourse(CourseDTO courseDTO, long id) {
        Course existing = courseDao.getById(id);
        if (existing == null) {
            log.warn("课程不存在");
            throw new RuntimeException("课程不存在");
        }

        BeanUtils.copyProperties(courseDTO, existing);
        existing.setId(id);

        boolean updated = courseDao.updateById(existing);
        if (!updated) {
            log.warn("课程更新失败");
            throw new RuntimeException("更新失败");
        }

        // 先删除所有旧的教师关系
        teacherCourseDao.remove(new QueryWrapper<TeacherCourse>().eq("course_id", id));

        // 再插入新的教师关系（如果 teachersId 不为空）
        if (courseDTO.getTeachersId() != null && !courseDTO.getTeachersId().isEmpty()) {
            // 校验教师是否存在
            List<Long> teacherIds = courseDTO.getTeachersId();
            List<Long> existTeacherIds = teacherDao.listObjs(new QueryWrapper<Teacher>().in("id", teacherIds))
                    .stream().map(o -> (Long) o).toList();
            for (Long teacherId : teacherIds) {
                if (!existTeacherIds.contains(teacherId)) {
                    log.warn("教师不存在, ID: {}", teacherId);
                    throw new RuntimeException("教师ID不存在: " + teacherId);
                }
                teacherCourseDao.save(new TeacherCourse(id, teacherId));
            }
        }

        log.info("课程更新成功");
    }

    @Override
    public void deleteCourse(long id) {
        boolean removed = courseDao.removeById(id);
        if (!removed) {
            log.warn("课程删除失败");
            throw new RuntimeException("删除失败，课程不存在");
        }
        teacherCourseDao.remove(new QueryWrapper<TeacherCourse>().eq("course_id", id));
        log.info("课程删除成功");
    }

    @Override
    public CourseVO getCourseById(long id) {
        Course course = courseDao.getById(id);
        if (course == null) {
            log.warn("课程不存在, ID: {}", id);
            throw new RuntimeException("课程不存在");
        }
        CourseVO vo = new CourseVO();
        BeanUtils.copyProperties(course, vo);
        // 查询教师列表
        List<Long> teacherIds = teacherCourseDao.listObjs(
                new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getCourseId, course.getId()).select(TeacherCourse::getTeacherId),
                obj -> (Long) obj
        );
        vo.setTeachers(teacherIds);

        // 需要教师姓名
        List<String> teacherNames = teacherDao.listObjs(
                new LambdaQueryWrapper<Teacher>().in(Teacher::getId, teacherIds).select(Teacher::getName),
                obj -> (String) obj
        );
        vo.setTeachersName(teacherNames);
        return vo;
    }

    @Override
    public List<CourseVO> getCourses(long page, long size, String keyword) {
        Page<Course> pageParam = new Page<>(page, size);

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword);
        }

        Page<Course> resultPage = courseDao.page(pageParam, queryWrapper);

        List<CourseVO> voList = resultPage.getRecords().stream().map(course -> {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(course, vo);

            // 查询教师列表
            List<Long> teacherIds = teacherCourseDao.listObjs(
                    new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getCourseId, course.getId()).select(TeacherCourse::getTeacherId),
                    obj -> (Long) obj
            );
            vo.setTeachers(teacherIds);

            // 需要教师姓名
            List<String> teacherNames = teacherDao.listObjs(
                    new LambdaQueryWrapper<Teacher>().in(Teacher::getId, teacherIds).select(Teacher::getName),
                    obj -> (String) obj
            );
            vo.setTeachersName(teacherNames);

            return vo;
        }).collect(Collectors.toList());
        
        return voList;
    }
}
