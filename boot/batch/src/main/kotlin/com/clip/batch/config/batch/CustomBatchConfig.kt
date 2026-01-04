package com.clip.batch.config.batch

import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository
import org.springframework.batch.core.configuration.support.MapJobRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
@EnableJdbcJobRepository
class CustomBatchConfig {

    @Bean
    fun jobRegistry(): JobRegistry {
        return MapJobRegistry()
    }
}