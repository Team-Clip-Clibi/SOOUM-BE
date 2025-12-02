package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.clip.api.notification.event.FollowFCMEvent
import com.clip.data.member.entity.DeviceType
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Component

@Component
class FollowFcmMsgGenerator: FcmMsgGenerator {
    companion object {
        private const val FOLLOW_SUFFIX = "님이 회원님을 팔로우합니다."
    }
    override fun generateMsg(fcmEvent: FCMEvent): Message {
        fcmEvent as FollowFCMEvent
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateGeneralMsgByIos(fcmEvent)
        } else {
            generateGeneralMsgByAos(fcmEvent)
        }
    }

    private fun generateGeneralMsgByIos(fcmDto: FollowFCMEvent): Message {
        val data = toFollowFcmData(fcmDto)

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.TITLE)
                    .setBody(generateFollowMsgBody(fcmDto.followingNickname, fcmDto.notificationType))
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

    private fun generateGeneralMsgByAos(fcmDto: FollowFCMEvent): Message {
        val data = toFollowFcmData(fcmDto)

        return Message.builder()
            .putAllData(data + mapOf("title" to FcmMsgGenerator.TITLE,
                "body" to generateFollowMsgBody(fcmDto.followingNickname, fcmDto.notificationType)))
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun toFollowFcmData(
        followFCMEvent: FollowFCMEvent
    ): Map<String, String> = mapOf(
        "notificationId" to followFCMEvent.notificationId.toString(),
        "followingMemberId" to followFCMEvent.followingMemberId.toString(),
        "followingMemberNickname" to followFCMEvent.followingNickname,
        "notificationType" to followFCMEvent.notificationType.name
    )

    private fun generateFollowMsgBody(
        publisherName: String,
        notificationType: NotificationType
    ): String = publisherName + when (notificationType) {
        NotificationType.FOLLOW -> FOLLOW_SUFFIX
        else -> throw IllegalArgumentException("Unsupported notificationType: $notificationType")
    }

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is FollowFCMEvent
    }
}