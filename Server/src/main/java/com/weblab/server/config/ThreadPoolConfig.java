package com.weblab.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
public class ThreadPoolConfig {

    private static final String NOTIFICATION_EXECUTOR = "notificationExecutor"; // 通知线程池

    @Bean(NOTIFICATION_EXECUTOR)
    public ExecutorService notificationExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-notification-");
        executor.initialize();
        return executor;
    }
}
