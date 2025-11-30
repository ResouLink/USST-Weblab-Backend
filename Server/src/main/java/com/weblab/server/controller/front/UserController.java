package com.weblab.server.controller.front;

import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.entity.Users;
import com.weblab.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 */

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户id 查找 用户
     *
     * @param id
     * @return
     */
    @RequestMapping("get/{id}")
    public ApiResult<Users> getById(@PathVariable Long id) {
        return ApiResult.success(userService.getById(id));
    }


}
