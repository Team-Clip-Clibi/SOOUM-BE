package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.clip.api.notification.event.SystemFCMEvent
import com.clip.data.member.entity.DeviceType
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.google.firebase.messaging.*
import org.springframework.stereotype.Component

@Component
class SystemFcmMsgGenerator: FcmMsgGenerator {
    companion object {
        private const val BLOCKED_BODY = "지속적인 신고로 글쓰기 제한됐습니다."
        private const val DELETED_BODY = "신고로 인해 카드가 삭제 처리됐습니다."
        private const val TRANSFER_SUCCESS_BODY = "계정이관이 성공적으로 완료되었습니다."
    }

    override fun generateMsg(fcmEvent: FCMEvent): Message {
        fcmEvent as SystemFCMEvent
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateSystemMsgByIos(fcmEvent)
        } else {
            generateSystemMsgByAos(fcmEvent)
        }
    }

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is SystemFCMEvent
    }

    private fun generateSystemMsgByIos(fcmDto: SystemFCMEvent): Message {
        val data = toSystemFcmData(fcmDto)

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.Companion.TITLE)
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

    private fun generateSystemMsgByAos(fcmDto: SystemFCMEvent): Message {
        val data = toSystemFcmData(fcmDto)

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.Companion.TITLE)
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

    private fun generateSystemMsgBody(notificationType: NotificationType): String = when (notificationType) {
        NotificationType.BLOCKED -> BLOCKED_BODY
        NotificationType.DELETED -> DELETED_BODY
        NotificationType.TRANSFER_SUCCESS -> TRANSFER_SUCCESS_BODY
        else -> throw IllegalArgumentException("Unsupported notificationType: $notificationType")
    }

    private fun toSystemFcmData(
        systemFCMEvent: SystemFCMEvent
    ): Map<String, String> = mapOf(
        "notificationId" to systemFCMEvent.notificationId.toString(),
        "expiredAt" to (systemFCMEvent.expiredAt?.toString()?:"null"),
        "notificationType" to systemFCMEvent.notificationType.name
    )
}