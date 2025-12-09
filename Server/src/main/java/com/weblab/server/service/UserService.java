package com.weblab.server.service;

import com.weblab.server.dto.UserRegisterDTO;
import com.weblab.server.dto.UserUpdateDTO;
import com.weblab.server.entity.Users;
import com.weblab.server.vo.UserVO;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void addUserStudent(UserRegisterDTO userRegisterDTO) throws IOException;
    void addUserTeacher(UserRegisterDTO userRegisterDTO) throws IOException;
    void updateUser(UserUpdateDTO userUpdateDTO, long id);
    void deleteUser(long id);
    UserVO getUserById(long id);
    List<UserVO> getUsers(long page, long size, String keyword);
}
