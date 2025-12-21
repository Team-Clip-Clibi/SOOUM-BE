package com.clip.global.config.fcm

import com.clip.infra.s3.S3FcmService
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.io.InputStream

@Configuration
class FcmConfig(
    private val s3FcmService: S3FcmService,
) {
    private val logger = KotlinLogging.logger {}
    @PostConstruct
    fun initialize() {
        if (!FirebaseApp.getApps().isEmpty()) {
            logger.info { "FirebaseApp already initialized" }
            return
        }

        try {
            val sdk: InputStream = s3FcmService.findFcmSdk()
            FirebaseApp.initializeApp(fcmOptions(sdk))
            logger.info { "FirebaseApp initialized" }
        } catch (e: IOException) {
            logger.error{e.message}
            throw RuntimeException(e)
        }
    }

    private fun fcmOptions(sdk: InputStream): FirebaseOptions {
        return FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(sdk))
            .build()
    }
}