package com.weblab.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.common.exception.ServiceException;
import com.weblab.server.dao.TeacherCourseDao;
import com.weblab.server.dao.TeacherDao;
import com.weblab.server.dto.PageDto;
import com.weblab.server.dto.TeacherDTO;
import com.weblab.server.entity.Teacher;
import com.weblab.server.entity.TeacherCourse;
import com.weblab.server.service.TeacherService;
import com.weblab.server.vo.TeacherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private TeacherCourseDao teacherCourseDao;

    @Override
    public Teacher getById(Long id) {
        Teacher teacher = teacherDao.getById(id);
        if (BeanUtil.isEmpty(teacher)){
            throw new ServiceException("教师ID不存在: " + id);
        }
        return teacher;
    }

    /**
     * 获取教师列表
     * @param pageDto
     * @return
     */
    @Override
    public List<Teacher> list(PageDto pageDto) {
        if (BeanUtil.isEmpty(pageDto)){
            throw new ServiceException("参数不能为空");
        }
        // 获取分页数据
        Page<Teacher> page = teacherDao.page(new Page<>(pageDto.getPage(), pageDto.getSize()));
        return page.getRecords();
    }

    @Override
    public Boolean save(Teacher teacher) {
        if (BeanUtil.isEmpty(teacher)){
            throw new ServiceException("新增教师为空!");
        }
        return teacherDao.save(teacher);
    }

    /**
     * 修改教师信息（简介，职称等）
     * @param teacher
     * @return
     */
    @Override
    public Boolean update(Teacher teacher) {
        if (BeanUtil.isEmpty(teacher)){
            throw new ServiceException("修改教师为空!");
        }
        return teacherDao.updateById(teacher);
    }

    @Override
    public Boolean delete(Long id) {
        if (BeanUtil.isEmpty(id)) {
            throw new ServiceException("删除id为空!");
        }
        return teacherDao.removeById(id);
    }

    /**
     * 添加老师讲授课程
     * @param teacherId
     * @param courseId
     * @return
     */
    @Override
    public Boolean addTeachCourse(Long teacherId, Long courseId) {
        if (BeanUtil.isEmpty(teacherId) || BeanUtil.isEmpty(courseId)){
            throw new ServiceException("参数不能为空");
        }
        TeacherCourse teacherCourse = new TeacherCourse(courseId, teacherId);
        return teacherCourseDao.save(teacherCourse);
    }

    /**
     * 删除老师讲授课程
     * @param teacherId
     * @param courseId
     * @return
     */
    @Override
    public Boolean deleteTeachCourse(Long teacherId, Long courseId) {
        if (BeanUtil.isEmpty(teacherId) || BeanUtil.isEmpty(courseId)){
            throw new ServiceException("参数不能为空");
        }
        return teacherCourseDao.remove(new QueryWrapper<TeacherCourse>().eq("teacher_id", teacherId).eq("course_id", courseId));
    }

    /**
     * 获取老师所授课程
     * @param id
     * @return
     */
    @Override
    public List<Long> getTeachCourses(Long id) {
        if (BeanUtil.isEmpty(id)){
            throw new ServiceException("参数不能为空");
        }
        return teacherCourseDao.list(new QueryWrapper<TeacherCourse>().eq("teacher_id", id)).stream().map(TeacherCourse::getCourseId).toList();
    }


}
