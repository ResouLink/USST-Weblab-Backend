package com.weblab.server.config;

import com.weblab.server.security.LoginUser;
import com.weblab.server.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    /**
     * ⚠️ 明文密码（仅学习阶段可用）
     */
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    /**
     * Spring Security 6 获取 AuthenticationManager 的标准方式
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * ⭐⭐⭐ 最粗暴、最稳妥的 CORS 配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 前端地址（Vite）
        config.addAllowedOrigin("http://localhost:5173");

        // 全部放行
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        // ⭐ Session 必须开启
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * ⭐⭐⭐ 核心安全配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1️⃣ 彻底关闭 CSRF（前后端分离必关）
                .csrf(csrf -> csrf.disable())

                // 2️⃣ 启用 CORS（必须）
                .cors(cors -> {})

                // 3️⃣ 授权规则
                .authorizeHttpRequests(auth -> auth
                        // ⭐ 放行预检请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 学生注册
                        .requestMatchers(HttpMethod.POST, "/api/users/students").permitAll()
                        // 登录 & 注册接口
                        .requestMatchers("/api/login").permitAll()
                        // SSE 连接接口
                        .requestMatchers("/api/sse/connect").permitAll()

                        // 业务接口必须登录
                        .requestMatchers("/api/**").authenticated()

                        // 其它全部放行
                        .anyRequest().permitAll()
                )

                // 4️⃣ 指定用户加载逻辑
                .userDetailsService(userDetailsService)

                // 5️⃣ ⭐⭐⭐ 开启表单登录（解决 /login 404 的关键）
                .formLogin(form -> form
                        // 前端 POST 的地址
                        .loginProcessingUrl("/api/login")

                        // 登录成功
                        .successHandler((request, response, authentication) -> {

                            // 1️⃣ 拿到当前登录用户
                            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

                            // 2️⃣ 角色（如 ROLE_TEACHER / ROLE_STUDENT / ROLE_ADMIN）
                            String role = loginUser.getAuthorities()
                                    .iterator()
                                    .next()
                                    .getAuthority();

                            // 3️⃣ 返回给前端
                            String json = """
                            {
                              "code": 200,
                              "msg": "登录成功",
                              "data": {
                                "username": "%s",
                                "role": "%s",
                                "is_personal_information_completed" : %s
                              }
                            }
                            """.formatted(loginUser.getUsername(),role, loginUser.getUser().getRoleId() != null);

                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(json);
                        })

                        // 登录失败
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter()
                                    .write("{\"code\":401,\"msg\":\"用户名或密码错误\"}");
                        })

                        .permitAll()
                )

                // 6️⃣ 未登录访问受保护接口
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter()
                                    .write("{\"code\":401,\"msg\":\"未登录\"}");
                        })
                )

                // 7️⃣ 退出登录
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter()
                                    .write("{\"code\":200,\"msg\":\"退出成功\"}");
                        })
                );

        return http.build();
    }
}


