package com.clip.api.notification.event.fcmsender

import com.clip.global.exception.FCMException
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MessagingErrorCode
import org.slf4j.LoggerFactory
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class FcmSender {
    companion object {
        private val log = LoggerFactory.getLogger(FcmSender::class.java)
    }
    @Retryable(includes = [FCMException::class], maxRetries = 3, delay = 1000, multiplier = 2.0)
    fun send(message: Message) {
        try {
            FirebaseMessaging.getInstance().send(message)
        } catch (e: FirebaseMessagingException) {
            if (isRetryable(e.messagingErrorCode)) {
                log.warn(
                    "FCM retryable error. code={}, msg={}",
                    e.messagingErrorCode, e.message
                )
                throw FCMException()
            } else {
                log.error(
                    "FCM non-retryable error. code={}, msg={}",
                    e.messagingErrorCode, e.message, e
                )
            }
        }
    }

    private fun isRetryable(code: MessagingErrorCode?): Boolean = when (code) {
        MessagingErrorCode.INTERNAL,
        MessagingErrorCode.UNAVAILABLE,
        MessagingErrorCode.QUOTA_EXCEEDED -> true
        else -> false
    }
}