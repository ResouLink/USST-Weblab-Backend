package com.weblab.server.service.impl;

import com.weblab.common.result.ApiResult;
import com.weblab.server.dao.UserDao;
import com.weblab.server.entity.Users;
import com.weblab.server.service.UserService;
import com.weblab.server.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public ApiResult getUserById(long id) {
        Users user = userDao.getById(id);
        if (user == null) {
            log.warn("用户不存在, ID: {}", id);
            return ApiResult.fail("用户不存在");
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        log.info("获取用户成功, ID: {}", id);
        return ApiResult.success(vo);
    }
}
