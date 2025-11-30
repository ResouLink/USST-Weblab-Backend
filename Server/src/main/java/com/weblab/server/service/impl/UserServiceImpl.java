package com.weblab.server.service.impl;


import com.weblab.server.dao.UserDao;
import com.weblab.server.entity.Users;
import com.weblab.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public Users getById(Long id) {
        return userDao.getById(id);
    }
}
