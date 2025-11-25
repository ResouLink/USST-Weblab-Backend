package com.weblab.server.controller;

import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.entity.User;
import com.weblab.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.weblab.common.core.domain.ApiResult.success;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户id 查找 用户
     * @param id
     * @return
     */
    @RequestMapping("get/{id}")
    public ApiResult<User> getById(@PathVariable Long id){
        return ApiResult.success(userService.getById(id));
    }


}
