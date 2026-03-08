package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.clip.api.notification.event.FollowerCardUploadFCMEvent
import com.clip.data.member.entity.DeviceType
import com.google.firebase.messaging.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class FollowerCardUploadFcmMsgGenerator: FcmMsgGenerator {

    companion object {
        private const val FOLLOWER_SUFFIX = "님의 새로운 카드가 올라왔어요:"
    }
    override fun generateMsg(fcmEvent: FCMEvent): Message {
        fcmEvent as FollowerCardUploadFCMEvent
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateGeneralMsgByIos(fcmEvent)
        } else {
            generateGeneralMsgByAos(fcmEvent)
        }
    }

    private fun generateGeneralMsgByIos(cardUploadFcmEvent: FollowerCardUploadFCMEvent): Message {

        val hasImage = !cardUploadFcmEvent.userImgUrl.isNullOrBlank()

        val apnsConfigBuilder = ApnsConfig.builder()
            .putHeader("apns-collapse-id", cardUploadFcmEvent.notificationId.toString())
            .setAps(
                Aps.builder()
                    .setMutableContent(hasImage)
                    .build()
            )

        if (hasImage) {
            apnsConfigBuilder.setFcmOptions(
                ApnsFcmOptions.builder()
                    .setImage(cardUploadFcmEvent.userImgUrl)
                    .build()
            )
        }

        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(FcmMsgGenerator.TITLE)
                    .setBody(generateFollowMsgBody(cardUploadFcmEvent.nickname, cardUploadFcmEvent.content))
                    .apply {
                        if (hasImage) {
                            setImage(cardUploadFcmEvent.userImgUrl)
                        }
                    }
                    .build()
            )
            .setApnsConfig(apnsConfigBuilder.build())
            .putAllData(toFollowFcmData(cardUploadFcmEvent))
            .setToken(cardUploadFcmEvent.fcmToken)
            .build()
    }

    private fun generateGeneralMsgByAos(fcmDto: FollowerCardUploadFCMEvent): Message {

        val messageBuilder = Message.builder()

        fcmDto.userImgUrl
            ?.takeIf { it.isNotBlank() }
            ?.let { imageUrl ->
                messageBuilder.setAndroidConfig(
                    AndroidConfig.builder()
                        .setNotification(
                            AndroidNotification.builder()
                                .setImage(imageUrl)
                                .build()
                        )
                        .build()
                )
            }

        return messageBuilder
            .putAllData(toFollowFcmData(fcmDto))
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun toFollowFcmData(
        followFCMEvent: FollowerCardUploadFCMEvent
    ): Map<String, String> = mapOf(
        "targetCardId" to followFCMEvent.targetCardId.toString(),
        "notificationType" to followFCMEvent.notificationType.name,
        "notificationId" to UUID.randomUUID().toString(),
        "title" to FcmMsgGenerator.TITLE,
        "body" to generateFollowMsgBody(followFCMEvent.nickname,followFCMEvent.content),
        "imageUrl" to (followFCMEvent.userImgUrl ?: "")
    )

    private fun generateFollowMsgBody(
        publisherName: String,
        content: String
    ): String {
        val lines = content.lines()
        val trimmed = lines.take(2).joinToString("\n")
        val suffix = if (lines.size > 2) "..." else ""

        return "$publisherName$FOLLOWER_SUFFIX $trimmed$suffix"
    }

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is FollowerCardUploadFCMEvent
    }
}