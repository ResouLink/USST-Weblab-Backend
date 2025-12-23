package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ApiResult addCourse(@RequestBody CourseDTO courseDTO) {
        try {
            courseService.addCourse(courseDTO);
            log.info("课程添加成功");
            return ApiResult.success("添加课程成功");
        } catch (Exception e) {
            log.error("课程添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult updateCourse(@PathVariable long id, @RequestBody CourseDTO courseDTO) {
        try {
            courseService.updateCourse(courseDTO, id);
            log.info("课程更新成功");
            return ApiResult.success("课程更新成功", 1);
        } catch (Exception e) {
            log.error("课程更新失败", e);
            return ApiResult.fail(1, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteCourse(@PathVariable long id) {
        try {
            courseService.deleteCourse(id);
            log.info("课程删除成功");
            return ApiResult.success("课程删除成功");
        } catch (Exception e) {
            log.error("课程删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResult getCourse(@PathVariable long id) {
        try {
            return ApiResult.success(courseService.getCourseById(id));
        } catch (Exception e) {
            log.error("获取课程失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping
    public ApiResult getCourses(@RequestParam long page, @RequestParam long size, @RequestParam(required = false) String keyword) {
        try {
            return ApiResult.success(courseService.getCourses(page, size, keyword));
        } catch (Exception e) {
            log.error("获取课程列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }
}
