package com.clip.api.notification.service

import com.clip.api.notification.event.NotificationFCMEvent
import com.clip.api.notification.event.SystemFCMEvent
import com.clip.data.member.entity.DeviceType
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Component

@Component
class FCMMessageGenerator {
    companion object {
        private const val TITLE = "Sooum"
        private const val COMMENT_WRITE_SUFFIX = "님이 답카드를 작성했습니다."
        private const val LIKE_SUFFIX = "님이 카드에 공감했습니다."
        private const val BLOCKED_BODY = "지속적인 신고로 글쓰기 제한됐습니다."
        private const val DELETED_BODY = "신고로 인해 카드가 삭제 처리됐습니다."
        private const val TRANSFER_SUCCESS_BODY = "계정이관이 성공적으로 완료되었습니다."
        private const val FOLLOW_SUFFIX = "님이 회원님을 팔로우합니다."
    }

    fun generateNotificationMsg(fcmDto: NotificationFCMEvent): Message {
        return if (fcmDto.deviceType == DeviceType.IOS) {
            generateGeneralMsgByIos(fcmDto)
        } else {
            generateGeneralMsgByAos(fcmDto)
        }
    }

    fun generateSystemMsg(fcmDto: SystemFCMEvent): Message {
        return if (fcmDto.deviceType == DeviceType.IOS) {
            generateSystemMsgByIos(fcmDto)
        } else {
            generateSystemMsgByAos(fcmDto)
        }
    }

    private fun generateGeneralMsgByAos(fcmDto: NotificationFCMEvent): Message {
        val data = toFcmData(
            notificationId = fcmDto.notificationId,
            targetCardPk = fcmDto.targetCardId,
            notificationType = fcmDto.notificationType
        )

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(TITLE)
                    .setBody(generateNotificationMsgBody(fcmDto.publisherNickname, fcmDto.notificationType))
                    .build()
            )
            .setAndroidConfig(
                AndroidConfig.builder()
                    .setNotification(
                        AndroidNotification.builder()
                            .setClickAction(fcmDto.notificationType.name)
                            .build()
                    )
                    .build()
            )
            .putAllData(data)
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun generateGeneralMsgByIos(fcmDto: NotificationFCMEvent): Message {
        val data = toFcmData(
            notificationId = fcmDto.notificationId,
            targetCardPk = fcmDto.targetCardId,
            notificationType = fcmDto.notificationType
        )

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(TITLE)
                    .setBody(generateNotificationMsgBody(fcmDto.publisherNickname, fcmDto.notificationType))
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

    private fun generateSystemMsgByAos(fcmDto: SystemFCMEvent): Message {
        val data = toSystemFcmData(
            notificationId = fcmDto.notificationId,
            notificationType = fcmDto.notificationType
        )

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(TITLE)
                    .setBody(generateSystemMsgBody(fcmDto.notificationType))
                    .build()
            )
            .setAndroidConfig(
                AndroidConfig.builder()
                    .setNotification(
                        AndroidNotification.builder()
                            .setClickAction(fcmDto.notificationType.name)
                            .build()
                    )
                    .build()
            )
            .putAllData(data)
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun generateSystemMsgByIos(fcmDto: SystemFCMEvent): Message {
        val data = toSystemFcmData(
            notificationId = fcmDto.notificationId,
            notificationType = fcmDto.notificationType
        )

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(TITLE)
                    .setBody(generateSystemMsgBody(fcmDto.notificationType))
                    .build()
            )
            .setApnsConfig(
                ApnsConfig.builder()
                    .setAps(
                        Aps.builder()
                            .setContentAvailable(true)
                            .build()
                    )
                    .build()
            )
            .putAllData(data)
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun toFcmData(
        notificationId: Long,
        targetCardPk: Long,
        notificationType: NotificationType
    ): Map<String, String> = mapOf(
        "notificationId" to notificationId.toString(),
        "targetCardId" to targetCardPk.toString(),
        "notificationType" to notificationType.name
    )

    private fun toSystemFcmData(
        notificationId: Long,
        notificationType: NotificationType
    ): Map<String, String> = mapOf(
        "notificationId" to notificationId.toString(),
        "targetCardId" to "null",
        "notificationType" to notificationType.name
    )

    private fun generateNotificationMsgBody(
        publisherName: String,
        notificationType: NotificationType
    ): String = publisherName + when (notificationType) {
        NotificationType.COMMENT_WRITE -> COMMENT_WRITE_SUFFIX
        NotificationType.FEED_LIKE,
        NotificationType.COMMENT_LIKE -> LIKE_SUFFIX
        NotificationType.FOLLOW -> FOLLOW_SUFFIX
        else -> throw IllegalArgumentException("Unsupported notificationType: $notificationType")
    }

    private fun generateSystemMsgBody(notificationType: NotificationType): String = when (notificationType) {
        NotificationType.BLOCKED -> BLOCKED_BODY
        NotificationType.DELETED -> DELETED_BODY
        NotificationType.TRANSFER_SUCCESS -> TRANSFER_SUCCESS_BODY
        else -> throw IllegalArgumentException("Unsupported notificationType: $notificationType")
    }


}