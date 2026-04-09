package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.CardLikeFCMEvent
import com.clip.api.notification.event.CommentWriteCardFCMEvent
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
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateCardMsgByIos(fcmEvent)
        } else {
            generateCardMsgByAos(fcmEvent)
        }
    }

    private fun generateCardMsgByIos(fcmEvent: FCMEvent): Message {
        val data = toCardFcmData(fcmEvent)

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.TITLE)
                    .setBody(generateCardMsgBody(fcmEvent))
                    .build()
            )
            .setApnsConfig(
                ApnsConfig.builder()
                    .setAps(Aps.builder().build())
                    .putHeader("apns-collapse-id", fcmEvent.notificationId.toString())
                    .build()
            )
            .putAllData(data)
            .setToken(fcmEvent.fcmToken)
            .build()
    }

    private fun generateCardMsgByAos(fcmEvent: FCMEvent): Message {
        val data = toCardFcmData(fcmEvent)

        return Message.builder()
            .putAllData(data + mapOf("title" to FcmMsgGenerator.TITLE,
                "body" to generateCardMsgBody(fcmEvent)))
            .setToken(fcmEvent.fcmToken)
            .build()
    }

    private fun generateCardMsgBody(fcmEvent: FCMEvent): String = when (fcmEvent) {
        is CommentWriteCardFCMEvent -> {
            val commentBody = generateFeedCommentMsgBody(fcmEvent.commentContent)
            "${fcmEvent.writerNickname}$COMMENT_WRITE_SUFFIX:\"$commentBody\""
        }
        is CardLikeFCMEvent -> {
            if (fcmEvent.notificationType != NotificationType.FEED_LIKE &&
                fcmEvent.notificationType != NotificationType.COMMENT_LIKE
            ) {
                throw IllegalArgumentException("Unsupported notificationType: ${fcmEvent.notificationType}")
            }
            fcmEvent.writerNickname + LIKE_SUFFIX
        }
        else -> throw IllegalArgumentException("Unsupported fcmEvent: ${fcmEvent::class.simpleName}")
    }

    private fun generateFeedCommentMsgBody(commentContent: String): String {
        val lines = commentContent.lines()
        val trimmed = lines.take(2).joinToString("\n")
        val suffix = if (lines.size > 2) "..." else ""

        return "$trimmed$suffix"
    }

    private fun toCardFcmData(
        fcmEvent: FCMEvent
    ): Map<String, String> = mapOf(
        "notificationId" to fcmEvent.notificationId.toString(),
        "targetCardId" to getTargetCardId(fcmEvent).toString(),
        "notificationType" to fcmEvent.notificationType.name
    )

    private fun getTargetCardId(fcmEvent: FCMEvent): Long = when (fcmEvent) {
        is CommentWriteCardFCMEvent -> fcmEvent.targetCardId
        is CardLikeFCMEvent -> fcmEvent.targetCardId
        else -> throw IllegalArgumentException("Unsupported fcmEvent: ${fcmEvent::class.simpleName}")
    }

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is CommentWriteCardFCMEvent || fcmEvent is CardLikeFCMEvent
    }
}