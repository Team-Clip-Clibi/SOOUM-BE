package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.ArticleCardUploadFCMEvent
import com.clip.api.notification.event.FCMEvent
import com.clip.data.member.entity.DeviceType
import com.google.firebase.messaging.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class ArticleCardUploadFcmMsgGenerator: FcmMsgGenerator {
    companion object {
        private const val ARTICLE_PREFIX = "숨이가 새로운 질문을 올렸어요: "
    }
    override fun generateMsg(fcmEvent: FCMEvent): Message {
        fcmEvent as ArticleCardUploadFCMEvent
        return if (fcmEvent.deviceType == DeviceType.IOS) {
            generateGeneralMsgByIos(fcmEvent)
        } else {
            generateGeneralMsgByAos(fcmEvent)
        }
    }

    private fun generateGeneralMsgByIos(cardUploadFcmEvent: ArticleCardUploadFCMEvent): Message {
        val data = toArticleFcmData(cardUploadFcmEvent)

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
                    .setBody(generateFollowMsgBody(cardUploadFcmEvent.content))
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
                    "body" to generateFollowMsgBody(cardUploadFcmEvent.content),
                    "imageUrl" to (cardUploadFcmEvent.userImgUrl ?: "")
                )
            )
            .setToken(cardUploadFcmEvent.fcmToken)
            .build()
    }

    private fun generateGeneralMsgByAos(fcmDto: ArticleCardUploadFCMEvent): Message {

        val data = toArticleFcmData(fcmDto) + mapOf(
            "notificationId" to UUID.randomUUID().toString(),
            "title" to FcmMsgGenerator.TITLE,
            "body" to generateFollowMsgBody(fcmDto.content)
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

    private fun toArticleFcmData(
        followFCMEvent: ArticleCardUploadFCMEvent
    ): Map<String, String> = mapOf(
        "targetCardId" to followFCMEvent.targetCardId.toString(),
        "notificationType" to followFCMEvent.notificationType.name
    )

    private fun generateFollowMsgBody(
        content: String
    ): String {
        val lines = content.lines()
        val trimmed = lines.take(2).joinToString("\n")
        val suffix = if (lines.size > 2) "..." else ""

        return "$ARTICLE_PREFIX $trimmed$suffix"
    }

    override fun isSupported(fcmEvent: FCMEvent): Boolean {
        return fcmEvent is ArticleCardUploadFCMEvent
    }
}