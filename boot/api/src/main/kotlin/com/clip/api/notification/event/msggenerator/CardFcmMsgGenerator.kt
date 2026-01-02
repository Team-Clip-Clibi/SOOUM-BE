package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.CardFCMEvent
import com.clip.api.notification.event.FCMEvent
import com.clip.data.member.entity.DeviceType
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Component

@Component
class CardFcmMsgGenerator: FcmMsgGenerator {
    companion object {
        private const val COMMENT_WRITE_SUFFIX = "님이 댓글카드를 작성했습니다."
        private const val LIKE_SUFFIX = "님이 카드에 공감했습니다."
    }
    override fun generateMsg(fcmEvent: FCMEvent): Message {
        fcmEvent as CardFCMEvent
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateCardMsgByIos(fcmEvent)
        } else {
            generateCardMsgByAos(fcmEvent)
        }
    }

    private fun generateCardMsgByIos(fcmDto: CardFCMEvent): Message {
        val data = toCardFcmData(fcmDto)

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.TITLE)
                    .setBody(generateCardMsgBody(fcmDto.writerNickname, fcmDto.notificationType))
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

    private fun generateCardMsgByAos(fcmDto: CardFCMEvent): Message {
        val data = toCardFcmData(fcmDto)

        return Message.builder()
            .putAllData(data + mapOf("title" to FcmMsgGenerator.TITLE,
                "body" to generateCardMsgBody(fcmDto.writerNickname, fcmDto.notificationType)))
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun generateCardMsgBody(
        writerNickname: String,
        notificationType: NotificationType
    ): String = writerNickname + when (notificationType) {
        NotificationType.COMMENT_WRITE -> COMMENT_WRITE_SUFFIX
        NotificationType.FEED_LIKE,
        NotificationType.COMMENT_LIKE -> LIKE_SUFFIX
        else -> throw IllegalArgumentException("Unsupported notificationType: $notificationType")
    }

    private fun toCardFcmData(
        fcmEvent: CardFCMEvent
    ): Map<String, String> = mapOf(
        "notificationId" to fcmEvent.notificationId.toString(),
        "targetCardId" to fcmEvent.targetCardId.toString(),
        "notificationType" to fcmEvent.notificationType.name
    )

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is CardFCMEvent
    }
}