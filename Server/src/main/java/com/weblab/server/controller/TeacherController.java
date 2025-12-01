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

}
