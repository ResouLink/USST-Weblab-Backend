package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.server.dao.StudentCourseDao;
import com.weblab.server.dao.StudentDao;
import com.weblab.server.dto.StudentDTO;
import com.weblab.server.entity.Student;
import com.weblab.server.entity.StudentCourse;
import com.weblab.server.mapper.StudentCourseMapper;
import com.weblab.server.service.StudentService;
import com.weblab.server.vo.StudentVO;
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
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;
    private final StudentCourseDao studentCourseDao;

    @Override
    public long addStudent(StudentDTO studentDTO) {
        Student newStudent = new Student();
        BeanUtils.copyProperties(studentDTO, newStudent);
        studentDao.save(newStudent);
        return newStudent.getId();
    }

    @Override
    public void updateStudent(StudentDTO studentDTO,long id) {
        Student existing = studentDao.getById(id);
        if (existing == null) {
            log.warn("学生不存在");
            throw new RuntimeException("学生不存在");
        }

        // 将 DTO 的属性拷贝到实体对象中
        BeanUtils.copyProperties(studentDTO, existing);
        existing.setId(id); // 确保 ID 不被覆盖

        boolean updated = studentDao.updateById(existing);
        if (!updated) {
            log.warn("学生更新失败");
            throw new RuntimeException("更新失败");
        }
    }

    @Override
    public void deleteStudent(long id) {
        boolean removed = studentDao.removeById(id);
        if (!removed) {
            throw new RuntimeException("删除失败，学生不存在");
        }
        studentCourseDao.remove(new LambdaQueryWrapper<StudentCourse>().eq(StudentCourse::getStudentId,id));
    }

    @Override
    public StudentVO getStudentById(long id) {
        Student student = studentDao.getById(id);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        StudentVO vo = new StudentVO();
        BeanUtils.copyProperties(student, vo);
        return vo;
    }

    @Override
    public List<StudentVO> getStudents(long page, long size, String keyword) {
        // 1. 构建分页对象
        Page<Student> pageParam = new Page<>(page, size);

        // 2. 构建查询条件
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword);   // 按名字模糊搜索
        }

        // 3. 执行分页查询
        Page<Student> resultPage = studentDao.page(pageParam, queryWrapper);

        // 4. 转换为 VO
        List<StudentVO> voList = resultPage.getRecords().stream().map(student -> {
            StudentVO vo = new StudentVO();
            BeanUtils.copyProperties(student, vo);
            return vo;
        }).collect(Collectors.toList());
        // 5. 返回列表
        return voList;
    }

    @Override
    public void addStudentCourse(long studentId, long courseId) {
        studentCourseDao.save(StudentCourse.builder().studentId(studentId).courseId(courseId).build());
    }

    @Override
    public void deleteStudentCourse(long studentId, long courseId) {
        studentCourseDao.remove(new LambdaQueryWrapper<StudentCourse>()
                .eq(StudentCourse::getStudentId, studentId)
                .eq(StudentCourse::getCourseId, courseId));
    }

    @Override
    public List<Long> getStudentCourses(long studentId) {
        LambdaQueryWrapper<StudentCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentCourse::getStudentId, studentId).select(StudentCourse::getCourseId);
        return studentCourseDao.listObjs(queryWrapper,obj -> (Long) obj);
    }
}
