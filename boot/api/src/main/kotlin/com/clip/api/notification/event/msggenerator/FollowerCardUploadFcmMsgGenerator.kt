package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.clip.api.notification.event.FollowerCardUploadFCMEvent
import com.clip.data.member.entity.DeviceType
import com.google.firebase.messaging.*
import org.springframework.stereotype.Component

@Component
class FollowerCardUploadFcmMsgGenerator: FcmMsgGenerator {

    companion object {
        private const val FOLLOWER_SUFFIX = "님의 새로운 카드가 올라왔어요."
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
        val data = toFollowFcmData(cardUploadFcmEvent)

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
            .putAllData(
                data + mapOf(
                    "title" to FcmMsgGenerator.TITLE,
                    "body" to generateFollowMsgBody(cardUploadFcmEvent.nickname, cardUploadFcmEvent.content),
                    "imageUrl" to (cardUploadFcmEvent.userImgUrl ?: "")
                )
            )
            .setToken(cardUploadFcmEvent.fcmToken)
            .build()
    }

    private fun generateGeneralMsgByAos(fcmDto: FollowerCardUploadFCMEvent): Message {

        val data = toFollowFcmData(fcmDto) + mapOf(
            "title" to FcmMsgGenerator.TITLE,
            "body" to generateFollowMsgBody(fcmDto.nickname, fcmDto.content)
        )

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
            .putAllData(data)
            .setToken(fcmDto.fcmToken)
            .build()
    }

    private fun toFollowFcmData(
        followFCMEvent: FollowerCardUploadFCMEvent
    ): Map<String, String> = mapOf(
        "targetCardId" to followFCMEvent.targetCardId.toString(),
        "notificationType" to followFCMEvent.notificationType.name
    )

    private fun generateFollowMsgBody(
        publisherName: String,
        content: String
    ): String = "$publisherName$FOLLOWER_SUFFIX: $content"

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is FollowerCardUploadFCMEvent
    }
}