package com.weblab.server.config;

import com.weblab.server.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    //明文密码（你说不加密）
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    /**
     * Spring Security 6之后 AuthenticationManager 必须从 AuthenticationConfiguration 获取
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() //登录接口放行
                        .requestMatchers("/api/**").authenticated() //保护api下的所有接口
                        //预留角色控制（你后面写接口可以用）
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .userDetailsService(userDetailsService) //告诉security怎么查用户
                .formLogin(form -> form
                        //.loginPage("/login") //可以自定义登录页（可删）
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
