package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.TeacherDTO;
import com.weblab.server.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ApiResult addTeacher(@RequestBody TeacherDTO teacherDTO) {
        return teacherService.addTeacher(teacherDTO);
    }

    @PutMapping("/{id}")
    public ApiResult updateTeacher(@PathVariable long id, @RequestBody TeacherDTO teacherDTO) {
        return teacherService.updateTeacher(teacherDTO, id);
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteTeacher(@PathVariable long id) {
        return teacherService.deleteTeacher(id);
    }

    @GetMapping("/{id}")
    public ApiResult getTeacher(@PathVariable long id) {
        return teacherService.getTeacherById(id);
    }

    @GetMapping
    public ApiResult getTeachers(@RequestParam long page, @RequestParam long size, @RequestParam String keyword) {
        return teacherService.getTeachers(page, size, keyword);
    }

    @PostMapping("/{teacher_id}/courses/{course_id}")
    public ApiResult addTeacherCourse(@PathVariable("teacher_id") long teacherId,@PathVariable("course_id") long courseId) {
        try {
            teacherService.addTeacherCourse(teacherId, courseId);
            return ApiResult.success(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.fail("添加失败");
    }

    @DeleteMapping("/{teacher_id}/courses/{courses_id}")
    public ApiResult deleteTeacherCourse(@PathVariable("teacher_id") long teacherId, @PathVariable("courses_id") long courseId)  {
        try {
            teacherService.deleteTeacherCourse(teacherId, courseId);
            return ApiResult.success(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.fail("删除失败");
    }

    @GetMapping("/{id}/courses")
    public ApiResult getTeacherCourses(@PathVariable("id") long teacherId) {
        return ApiResult.success(teacherService.getTeacherCourses(teacherId));
    }

}
