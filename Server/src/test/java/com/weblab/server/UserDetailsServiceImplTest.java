package com.weblab.server;

import com.weblab.server.dao.UserDao;
import com.weblab.server.entity.Users;
import com.weblab.server.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserDao userDao; // 真实访问数据库

    @Test
    void testLoadUserByUsername_found() {
        // 先确保数据库里有 admin 用户
        Users user = userDao.getByUsername("admin");
        Assertions.assertNotNull(user, "数据库中必须有 admin 用户");

        // 调用 UserDetailsServiceImpl
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("admin", userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_notFound() {
        // 数据库里不存在这个用户名
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("non_exist_user"));
    }


}

