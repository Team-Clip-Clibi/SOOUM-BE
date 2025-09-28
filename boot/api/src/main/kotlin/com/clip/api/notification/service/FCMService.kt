package com.clip.api.notification.service

import com.clip.api.notification.event.NotificationFCMEvent
import com.clip.api.notification.event.SystemFCMEvent
import org.springframework.stereotype.Service

@Service
class FCMService(
    private val fcmMessageGenerator: FCMMessageGenerator,
    private val fcmSender: FCMSender
) {
    fun sendSystemFCM(fcmEvent: SystemFCMEvent) {
        val msg = fcmMessageGenerator.generateSystemMsg(fcmEvent)
        fcmSender.send(msg)
    }

    fun sendNotificationFCM(fcmEvent: NotificationFCMEvent) {
        val msg = fcmMessageGenerator.generateNotificationMsg(fcmEvent)
        fcmSender.send(msg)
    }
}