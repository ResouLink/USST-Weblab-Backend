package com.weblab.server.service;

import com.weblab.server.entity.Users;

public interface UserService {
    Users getById(Long id);
}
