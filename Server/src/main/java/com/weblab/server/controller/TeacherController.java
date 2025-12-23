package com.weblab.server.controller;

import com.weblab.common.enums.RoleEnum;
import com.weblab.common.result.ApiResult;
import com.weblab.server.dao.UserDao;
import com.weblab.server.dto.TeacherDTO;
import com.weblab.server.security.SecurityUtil;
import com.weblab.server.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final UserDao userDao;
    @PostMapping
    public ApiResult addTeacher(@RequestBody TeacherDTO teacherDTO) {
        try {
            long newTeaId = teacherService.addTeacher(teacherDTO);
            if(Objects.equals(SecurityUtil.getRole(), RoleEnum.TEACHER.value())) {
                userDao.setRoleIfEmpty(SecurityUtil.getUserId(), newTeaId);
            }
            log.info("教师添加成功");
            return ApiResult.success("添加教师成功");
        } catch (Exception e) {
            log.error("教师添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult updateTeacher(@PathVariable long id, @RequestBody TeacherDTO teacherDTO) {
        try {
            teacherService.updateTeacher(teacherDTO, id);
            log.info("教师更新成功");
            return ApiResult.success("教师更新成功", 1);
        } catch (Exception e) {
            log.error("教师更新失败", e);
            return ApiResult.fail(1, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteTeacher(@PathVariable long id) {
        try {
            teacherService.deleteTeacher(id);
            log.info("教师删除成功");
            return ApiResult.success("教师删除成功");
        } catch (Exception e) {
            log.error("教师删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResult getTeacher(@PathVariable long id) {
        try {
            return ApiResult.success(teacherService.getTeacherById(id));
        } catch (Exception e) {
            log.error("获取教师失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping
    public ApiResult getTeachers(@RequestParam long page, @RequestParam long size, @RequestParam(required = false) String keyword) {
        try {
            return ApiResult.success(teacherService.getTeachers(page, size, keyword));
        } catch (Exception e) {
            log.error("获取教师列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PostMapping("/{teacher_id}/courses/{course_id}")
    public ApiResult addTeacherCourse(@PathVariable("teacher_id") long teacherId, @PathVariable("course_id") long courseId) {
        try {
            teacherService.addTeacherCourse(teacherId, courseId);
            log.info("教师课程关系添加成功");
            return ApiResult.success("添加成功", 1);
        } catch (Exception e) {
            log.error("教师课程关系添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @DeleteMapping("/{teacher_id}/courses/{course_id}")
    public ApiResult deleteTeacherCourse(@PathVariable("teacher_id") long teacherId, @PathVariable("course_id") long courseId) {
        try {
            teacherService.deleteTeacherCourse(teacherId, courseId);
            log.info("教师课程关系删除成功");
            return ApiResult.success("删除成功", 1);
        } catch (Exception e) {
            log.error("教师课程关系删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}/courses")
    public ApiResult getTeacherCourses(@PathVariable("id") long teacherId) {
        try {
            return ApiResult.success(teacherService.getTeacherCourses(teacherId));
        } catch (Exception e) {
            log.error("获取教师课程列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

}
