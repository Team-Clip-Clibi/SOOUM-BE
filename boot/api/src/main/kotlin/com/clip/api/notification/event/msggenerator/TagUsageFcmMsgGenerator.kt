package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.clip.api.notification.event.TagUsageFcmEvent
import com.clip.data.member.entity.DeviceType
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Component

@Component
class TagUsageFcmMsgGenerator: FcmMsgGenerator {
    companion object {
        private const val TAG_USAGE_SUFFIX = "태그가 포함된 카드가 올라왔어요."
    }
    override fun generateMsg(fcmEvent: FCMEvent): Message {
        fcmEvent as TagUsageFcmEvent
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateCardMsgByIos(fcmEvent)
        } else {
            generateCardMsgByAos(fcmEvent)
        }
    }

    private fun generateCardMsgByIos(fcmDto: TagUsageFcmEvent): Message {
        val data = toCardFcmData(fcmDto)

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.TITLE)
                    .setBody(generateCardMsgBody(fcmDto.tagContent))
                    .build()
            )
            .setApnsConfig(
                ApnsConfig.builder()
                    .setAps(Aps.builder().build())
                    .putHeader("apns-collapse-id", fcmDto.notificationId.toString())
                    .build()
            )
            .putAllData(data)
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun generateCardMsgByAos(fcmDto: TagUsageFcmEvent): Message {
        val data = toCardFcmData(fcmDto)

        return Message.builder()
            .putAllData(data + mapOf("title" to FcmMsgGenerator.TITLE,
                "body" to generateCardMsgBody(fcmDto.tagContent)))
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun generateCardMsgBody(tagContent: String): String = "'${tagContent}' " + TAG_USAGE_SUFFIX

    private fun toCardFcmData(
        fcmEvent: TagUsageFcmEvent
    ): Map<String, String> = mapOf(
        "notificationId" to fcmEvent.notificationId.toString(),
        "targetCardId" to fcmEvent.targetCardId.toString(),
        "notificationType" to fcmEvent.notificationType.name
    )

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is TagUsageFcmEvent
    }
}