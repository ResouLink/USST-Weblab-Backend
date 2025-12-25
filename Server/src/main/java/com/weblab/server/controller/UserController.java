package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.UserRegisterDTO;
import com.weblab.server.dto.UserUpdateDTO;
import com.weblab.server.security.SecurityUtil;
import com.weblab.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/students")
    public ApiResult addStudentUser(@RequestBody UserRegisterDTO userRegisterDTO) throws IOException {
        userService.addUserStudent(userRegisterDTO);
        return ApiResult.success("1");
    }
    @PostMapping("/teachers")
    public ApiResult addTeacherUser(@RequestBody UserRegisterDTO userRegisterDTO) throws IOException {
        userService.addUserTeacher(userRegisterDTO);
        return ApiResult.success("1");
    }
    @PutMapping("/{id}")
    public ApiResult updateUser(@RequestBody UserUpdateDTO userUpdateDTO, @PathVariable("id") long id) {
        if(id == -1) {
            id = SecurityUtil.getUserId();
        }
        userService.updateUser(userUpdateDTO, id);
        return ApiResult.success("1");
    }
    @DeleteMapping("/{id}")
    public ApiResult deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ApiResult.success("1");
    }

    @GetMapping("/{id}")
    public ApiResult getUser(@PathVariable long id) {
        if(id == -1) {
            id = SecurityUtil.getUserId();
        }
        return ApiResult.success(userService.getUserById(id));
    }
    @GetMapping
    public ApiResult getUsers(@RequestParam long page, @RequestParam long size, @RequestParam(required = false) String keyword) {
        return ApiResult.success(userService.getUsers(page, size, keyword));
    }
    @GetMapping("/students/{id}")
    public ApiResult getUsersByStudentId(@PathVariable("id") long studentId) {
        return ApiResult.success(userService.getUsersByStudentId(studentId));
    }

    @GetMapping("/teachers/{id}")
    public ApiResult getUsersByTeacherId(@PathVariable("id") long teacherId) {
        return ApiResult.success(userService.getUsersByTeacherId(teacherId));
    }
}
