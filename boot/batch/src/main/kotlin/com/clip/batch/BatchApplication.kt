package com.clip.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableBatchProcessing
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}
