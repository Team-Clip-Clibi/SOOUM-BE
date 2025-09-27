package com.clip

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
@ConfigurationPropertiesScan
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
