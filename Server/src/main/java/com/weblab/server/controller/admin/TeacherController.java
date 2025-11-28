package com.weblab.server.controller.admin;

import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.dto.PageDto;
import com.weblab.server.entity.Teacher;
import com.weblab.server.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers/")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("{id}")
    public ApiResult<Teacher> getById(@PathVariable Long id) {
        return ApiResult.success(teacherService.getById(id));
    }

    @GetMapping("list/{keyword}/{page}/{size}")
    public ApiResult<List<Teacher>> list(@PathVariable String keyword,
                                         @PathVariable long page,
                                         @PathVariable long size) {
        PageDto pageDto = new PageDto(keyword, page, size);
        return ApiResult.success(teacherService.list(pageDto));
    }
    @PostMapping
    public ApiResult<Boolean> save(@RequestBody Teacher teacher) {
        return ApiResult.success(teacherService.save(teacher));
    }

    @PutMapping("{id}")
    public ApiResult<Boolean> update(@PathVariable Long id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        return ApiResult.success(teacherService.update(teacher));
    }

    @DeleteMapping("{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return ApiResult.success(teacherService.delete(id));
    }

    @PutMapping("{teacher_id}/courses/{course_id}")
    public ApiResult<Boolean> addTeachCourse(@PathVariable Long teacher_id, @PathVariable Long course_id) {
        return ApiResult.success(teacherService.addTeachCourse(teacher_id, course_id));
    }

    @DeleteMapping("{teacher_id}/courses/{courses_id}")
    public ApiResult<Boolean> deleteTeachCourse(@PathVariable Long teacher_id, @PathVariable Long course_id) {
        return ApiResult.success(teacherService.deleteTeachCourse(teacher_id, course_id));
    }

    @GetMapping("{id}/courses")
    public ApiResult<List<Long>> getTeachCourses(@PathVariable Long id) {
        return ApiResult.success(teacherService.getTeachCourses(id));
    }
}
