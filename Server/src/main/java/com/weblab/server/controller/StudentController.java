package com.weblab.server.controller;


import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.StudentDTO;
import com.weblab.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ApiResult addStudent(@RequestBody StudentDTO studentDTO) {

        return studentService.addStudent(studentDTO);
    }

    @PutMapping("/{id}")
    public ApiResult updateStudent(@PathVariable long id,@RequestBody StudentDTO studentDTO) {

        return studentService.updateStudent(studentDTO, id);
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteStudent(@PathVariable long id) {

        return studentService.deleteStudent(id);
    }

    @GetMapping("/{id}")
    public ApiResult getStudent(@PathVariable long id) {

        return studentService.getStudentById(id);
    }

    @GetMapping()
    public ApiResult getStudents(@RequestParam long page,@RequestParam long size,@RequestParam String keyword) {
        return  studentService.getStudents(page,size,keyword);
    }

    @PostMapping("/{student_id}/courses/{course_id}")
    public ApiResult addStudentCourse(@PathVariable("student_id") long studentId,@PathVariable("course_id") long courseId) {
        studentService.addStudentCourse(studentId,courseId);
        return ApiResult.success(1);
    }

    @DeleteMapping("/{student_id}/courses/{course_id}")
    public ApiResult deleteStudentCourse(@PathVariable("student_id") long studentId, @PathVariable("course_id") long courseId) {
        studentService.deleteStudentCourse(studentId,courseId);
        return ApiResult.success(1);
    }

    @GetMapping("/{id}/courses")
    public ApiResult getStudentCourses(@PathVariable("id") long studentId) {
        return ApiResult.success(studentService.getStudentCourses(studentId));
    }
}
