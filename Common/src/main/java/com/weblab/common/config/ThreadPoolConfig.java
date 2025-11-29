package com.weblab.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {

    private static final String NOTIFICATION_EXECUTOR = "notificationExecutor"; // 通知线程池

    @Bean(name = NOTIFICATION_EXECUTOR)
    public ExecutorService notificationExecutor() {
        return Executors.newSingleThreadExecutor(); // 后续可以改
    }
}
