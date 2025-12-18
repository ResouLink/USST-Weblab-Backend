package com.weblab.server.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessListener {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) throws IOException, ServletException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        if (response != null) {
            customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, event.getAuthentication());
        }
    }
}
