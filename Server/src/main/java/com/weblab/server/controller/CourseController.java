package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.CourseDTO;
import com.weblab.server.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ApiResult addCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO);
    }

    @PutMapping("/{id}")
    public ApiResult updateCourse(@PathVariable long id, @RequestBody CourseDTO courseDTO) {
        return courseService.updateCourse(courseDTO, id);
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteCourse(@PathVariable long id) {
        return courseService.deleteCourse(id);
    }

    @GetMapping("/{id}")
    public ApiResult getCourse(@PathVariable long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping
    public ApiResult getCourses(@RequestParam long page, @RequestParam long size, @RequestParam String keyword) {
        return courseService.getCourses(page, size, keyword);
    }
}
