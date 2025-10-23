package com.clip.global.config.Async

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfig {

    @Bean(name = ["taskExecutor"])
    fun taskExecutor(): Executor = ThreadPoolTaskExecutor().apply {
        corePoolSize = 0
        maxPoolSize = Int.MAX_VALUE
        queueCapacity = 0
        keepAliveSeconds = 60
        setAllowCoreThreadTimeOut(true)
        setThreadNamePrefix("cached-async-")
        setWaitForTasksToCompleteOnShutdown(true)
        initialize()
    }
}