package com.weblab.server.security;

import cn.hutool.core.util.StrUtil;
import com.weblab.server.entity.Users;
import com.weblab.server.event.SseClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        Users user = loginUser.getUser();

        SseEmitter connect = SseClient.connect(StrUtil.toString(user.getId()));
        if (connect != null) {
            log.info("用户{}连接成功", user.getUsername());
        }
    }
}