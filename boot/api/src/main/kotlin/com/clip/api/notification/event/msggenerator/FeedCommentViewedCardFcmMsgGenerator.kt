package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.clip.api.notification.event.FeedCommentViewedCardFCMEvent
import com.clip.data.member.entity.DeviceType
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Component
import java.util.*

@Component
class FeedCommentViewedCardFcmMsgGenerator : FcmMsgGenerator {

    companion object {
        private const val COMMENT_ALERT_PREFIX = "내가 본 카드에 새로운 댓글이 달렸어요:"
    }

    override fun generateMsg(fcmEvent: FCMEvent): Message {
        fcmEvent as FeedCommentViewedCardFCMEvent
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateGeneralMsgByIos(fcmEvent)
        } else {
            generateGeneralMsgByAos(fcmEvent)
        }
    }

    private fun generateGeneralMsgByIos(fcmEvent: FeedCommentViewedCardFCMEvent): Message {
        val body = generateFeedCommentMsgBody(fcmEvent.commentContent)
        val data = toFeedCommentFcmData(fcmEvent)
        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.TITLE)
                    .setBody(body)
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

    private fun generateGeneralMsgByAos(fcmEvent: FeedCommentViewedCardFCMEvent): Message {
        return Message.builder()
            .putAllData(toFeedCommentFcmData(fcmEvent))
            .setToken(fcmEvent.fcmToken)
            .build()
    }

    private fun toFeedCommentFcmData(
        fcmEvent: FeedCommentViewedCardFCMEvent
    ): Map<String, String> = mapOf(
        "targetCardId" to fcmEvent.targetCardId.toString(),
        "notificationType" to fcmEvent.notificationType.name,
        "notificationId" to UUID.randomUUID().toString(),
        "title" to FcmMsgGenerator.TITLE,
        "body" to generateFeedCommentMsgBody(fcmEvent.commentContent)
    )

    private fun generateFeedCommentMsgBody(commentContent: String): String {
        val lines = commentContent.lines()
        val trimmed = lines.take(2).joinToString("\n")
        val suffix = if (lines.size > 2) "..." else ""

        return "$COMMENT_ALERT_PREFIX $trimmed$suffix"
    }

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is FeedCommentViewedCardFCMEvent
    }
}
