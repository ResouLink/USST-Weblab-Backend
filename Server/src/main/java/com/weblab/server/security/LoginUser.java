package com.weblab.server.security;

import com.weblab.common.enums.RoleEnum;
import com.weblab.server.entity.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class LoginUser implements UserDetails {

    private Users user;

    public LoginUser(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role;

        // 根据 userRole 预留三种角色
        if (user.getUserRole() == 0) {
            role = RoleEnum.TEACHER.value();
        } else if (user.getUserRole() == 1) {
            role = RoleEnum.STUDENT.value();
        } else {
            role = RoleEnum.ADMIN.value();
        }

        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();  // 或 user.getUsername()
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
