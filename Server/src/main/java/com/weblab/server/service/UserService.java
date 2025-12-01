package com.weblab.server.service;

import com.weblab.common.result.ApiResult;

public interface UserService {
    ApiResult getUserById(long id);
}
