package com.weblab.common.utils;

import com.weblab.server.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser)) {
            return null;
        }
        return (LoginUser) authentication.getPrincipal();
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUser().getId();
    }
    /**
     * 用法
     * String role = SecurityUtil.getRole();
     *
     * if (RoleEnum.TEACHER.value().equals(role)) {
     *     ...
     * }
     */
    public static String getRole() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) return null;
        return loginUser.getAuthorities().iterator().next().getAuthority();
    }
}



