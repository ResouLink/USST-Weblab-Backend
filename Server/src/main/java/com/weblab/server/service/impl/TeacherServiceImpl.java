package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.dao.TeacherDao;
import com.weblab.server.dto.TeacherDTO;
import com.weblab.server.entity.Teacher;
import com.weblab.server.entity.TeacherCourse;
import com.weblab.server.service.TeacherService;
import com.weblab.server.vo.TeacherVO;
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
public class TeacherServiceImpl implements TeacherService {
    private final TeacherDao teacherDao;
    private final TeacherCourseDao teacherCourseDao;

    @Override
    public long addTeacher(TeacherDTO teacherDTO) {
        Teacher newTeacher = new Teacher();
        BeanUtils.copyProperties(teacherDTO, newTeacher);
        teacherDao.save(newTeacher);
        log.info("教师添加成功");
        return newTeacher.getId();
    }

    @Override
    public void updateTeacher(TeacherDTO teacherDTO, long id) {
        Teacher existing = teacherDao.getById(id);
        if (existing == null) {
            log.warn("教师不存在");
            throw new RuntimeException("教师不存在");
        }

        BeanUtils.copyProperties(teacherDTO, existing);
        existing.setId(id);

        boolean updated = teacherDao.updateById(existing);
        if (!updated) {
            log.warn("教师更新失败");
            throw new RuntimeException("更新失败");
        }
        log.info("教师更新成功");
    }

    @Override
    public void deleteTeacher(long id) {
        boolean removed = teacherDao.removeById(id);
        if (!removed) {
            log.warn("教师删除失败");
            throw new RuntimeException("删除失败，教师不存在");
        }
        teacherCourseDao.remove(new QueryWrapper<TeacherCourse>().eq("teacher_id", id));
        log.info("教师删除成功");
    }

    @Override
    public TeacherVO getTeacherById(long id) {
        Teacher teacher = teacherDao.getById(id);
        if (teacher == null) {
            log.warn("教师不存在, ID: {}", id);
            throw new RuntimeException("教师不存在");
        }
        TeacherVO vo = new TeacherVO();
        BeanUtils.copyProperties(teacher, vo);
        return vo;
    }

    @Override
    public List<TeacherVO> getTeachers(long page, long size, String keyword) {
        Page<Teacher> pageParam = new Page<>(page, size);

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword);
        }

        Page<Teacher> resultPage = teacherDao.page(pageParam, queryWrapper);

        List<TeacherVO> voList = resultPage.getRecords().stream().map(teacher -> {
            TeacherVO vo = new TeacherVO();
            BeanUtils.copyProperties(teacher, vo);
            return vo;
        }).collect(Collectors.toList());

        return voList;
    }

    @Override
    public void addTeacherCourse(long teacherId, long courseId) throws Exception {
        teacherCourseDao.save(TeacherCourse.builder().teacherId(teacherId).courseId(courseId).build());
    }

    @Override
    public void deleteTeacherCourse(long teacherId, long courseId) throws Exception {
        teacherCourseDao.remove(new LambdaQueryWrapper<TeacherCourse>()
                .eq(TeacherCourse::getTeacherId, teacherId)
                .eq(TeacherCourse::getCourseId, courseId));
    }

    @Override
    public List<Long> getTeacherCourses(long teacherId) {
        LambdaQueryWrapper<TeacherCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeacherCourse::getTeacherId, teacherId).select(TeacherCourse::getCourseId);
        return teacherCourseDao.listObjs(queryWrapper, obj->(Long) obj);
    }

}
