package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Component

@Component
class FcmMsgStrategyRegistry(
    private val strategies: List<FcmMsgGenerator>,
) {
    fun getMessage(fcmEvent: FCMEvent): Message {
        val strategy = strategies.firstOrNull { it.isSupported(fcmEvent) }
            ?: throw UnsupportedOperationException("fcm event ${fcmEvent.notificationType} not supported.")
        return strategy.generateMsg(fcmEvent)
    }
}