package com.weblab.server.controller;

import com.weblab.common.enums.RoleEnum;
import com.weblab.common.result.ApiResult;
import com.weblab.server.security.SecurityUtil;
import com.weblab.server.dao.UserDao;
import com.weblab.server.dto.StudentDTO;
import com.weblab.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final UserDao userDao;

    @PostMapping
    public ApiResult addStudent(@RequestBody StudentDTO studentDTO) {
        try {
            long newStuId = studentService.addStudent(studentDTO);
            if(Objects.equals(SecurityUtil.getRole(), RoleEnum.STUDENT.value())){
                userDao.setRoleIfEmpty(SecurityUtil.getUserId(), newStuId);
            }
            log.info("学生添加成功");
            return ApiResult.success("添加学生成功");
        } catch (Exception e) {
            log.error("学生添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult updateStudent(@PathVariable long id, @RequestBody StudentDTO studentDTO) {
        try {
            studentService.updateStudent(studentDTO, id);
            log.info("学生更新成功");
            return ApiResult.success("学生更新成功", 1);
        } catch (Exception e) {
            log.error("学生更新失败", e);
            return ApiResult.fail(1, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteStudent(@PathVariable long id) {
        try {
            studentService.deleteStudent(id);
            log.info("学生删除成功");
            return ApiResult.success("学生删除成功");
        } catch (Exception e) {
            log.error("学生删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResult getStudent(@PathVariable long id) {
        try {
            return ApiResult.success(studentService.getStudentById(id));
        } catch (Exception e) {
            log.error("获取学生失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping()
    public ApiResult getStudents(@RequestParam long page, @RequestParam long size, @RequestParam(required = false) String keyword) {
        try {
            return ApiResult.success(studentService.getStudents(page, size, keyword));
        } catch (Exception e) {
            log.error("获取学生列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PostMapping("/{student_id}/courses/{course_id}")
    public ApiResult addStudentCourse(@PathVariable("student_id") long studentId, @PathVariable("course_id") long courseId) {
        try {
            studentService.addStudentCourse(studentId, courseId);
            log.info("学生课程关系添加成功");
            return ApiResult.success("添加成功", 1);
        } catch (Exception e) {
            log.error("学生课程关系添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @DeleteMapping("/{student_id}/courses/{course_id}")
    public ApiResult deleteStudentCourse(@PathVariable("student_id") long studentId, @PathVariable("course_id") long courseId) {
        try {
            studentService.deleteStudentCourse(studentId, courseId);
            log.info("学生课程关系删除成功");
            return ApiResult.success("删除成功", 1);
        } catch (Exception e) {
            log.error("学生课程关系删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}/courses")
    public ApiResult getStudentCourses(@PathVariable("id") long studentId) {
        try {
            return ApiResult.success(studentService.getStudentCourses(studentId));
        } catch (Exception e) {
            log.error("获取学生课程列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }
}
