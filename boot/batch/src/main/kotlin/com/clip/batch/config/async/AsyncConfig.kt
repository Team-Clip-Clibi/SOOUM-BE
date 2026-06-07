package com.clip.batch.config.async

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfig {

    @Bean(name = ["fcmTaskExecutor"])
    fun fcmTaskExecutor(): Executor = ThreadPoolTaskExecutor().apply {
        corePoolSize = 4
        maxPoolSize = 16
        queueCapacity = 1_000
        keepAliveSeconds = 60
        setAllowCoreThreadTimeOut(true)
        setThreadNamePrefix("fcm-async-")
        setWaitForTasksToCompleteOnShutdown(true)
        initialize()
    }
}
