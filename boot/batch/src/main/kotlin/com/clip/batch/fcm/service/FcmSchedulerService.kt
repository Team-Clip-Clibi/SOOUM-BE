package com.clip.batch.fcm.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class FcmSchedulerService {

    private val logger = KotlinLogging.logger {}

    @Async
    fun sendMulticastFcm(message: MulticastMessage) {
        runCatching {
            FirebaseMessaging.getInstance().sendEachForMulticast(message)
        }.onFailure {
            logger.error(it) { "==== FCM 메시지 전송 실패 에러 로그 ====" }
        }
    }
}