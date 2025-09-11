package com.clip.batch.rsa

import com.clip.batch.rsa.service.RsaSchedulerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.time.LocalDateTime
import io.github.oshai.kotlinlogging.KotlinLogging

@Component
class RsaScheduler(
    private val rsaSchedulerService: RsaSchedulerService,
) {
    private val logger = KotlinLogging.logger {}

//    @Scheduled(cron = "0 0 6 * * ?")
    @Scheduled(cron = "0 * * * * ?")
    fun schedule() {
        try {
            logger.info { "========RsaScheduler start========" }
            rsaSchedulerService.save(LocalDateTime.now())
        } catch (e: NoSuchAlgorithmException) {
            logger.info { e.message }
        }
    }
}