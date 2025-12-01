package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.service.UserService;
import com.weblab.server.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ApiResult getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }
}
