package com.weblab.server.security;

import com.weblab.server.dao.UserDao;
import com.weblab.server.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userDao.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new LoginUser(user);
    }
}
