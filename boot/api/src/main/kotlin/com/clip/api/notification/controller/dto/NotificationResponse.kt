package com.clip.api.notification.controller.dto

import com.clip.data.notification.entity.NotificationHistory
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

object NotificationDto {

    /**
     * 공통 알림 정보의 베이스. 서브타입 구분을 위해 sealed 사용.
     */
    sealed class CommonNotificationInfo(
        open val notificationId: Long,
        open val notificationType: NotificationType,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        open val createTime: LocalDateTime
    )

    data class FollowNotificationInfoResponse(
        override val notificationId: Long,
        override val notificationType: NotificationType,
        override val createTime: LocalDateTime,
        val nickName: String,
        val userId: Long
    ) : CommonNotificationInfo(notificationId, notificationType, createTime) {

        companion object {
            fun of(history: NotificationHistory): FollowNotificationInfoResponse =
                FollowNotificationInfoResponse(
                    notificationId = history.pk,
                    notificationType = history.notificationType,
                    createTime = history.createdAt,
                    nickName = history.fromMember.nickname,
                    userId = history.fromMember.pk
                )
        }
    }

    data class NotificationInfoResponse(
        override val notificationId: Long,
        override val notificationType: NotificationType,
        override val createTime: LocalDateTime,
        val targetCardId: Long,
        val nickName: String
    ) : CommonNotificationInfo(notificationId, notificationType, createTime) {

        companion object {
            fun of(history: NotificationHistory): NotificationInfoResponse =
                NotificationInfoResponse(
                    notificationId = history.pk,
                    targetCardId = history.targetCardPk,
                    notificationType = history.notificationType,
                    createTime = history.createdAt,
                    nickName = history.fromMember.nickname
                )
        }
    }

    data class DeleteNotificationInfoResponse(
        override val notificationId: Long,
        override val notificationType: NotificationType,
        override val createTime: LocalDateTime
    ) : CommonNotificationInfo(notificationId, notificationType, createTime) {

        companion object {
            fun of(history: NotificationHistory): DeleteNotificationInfoResponse =
                DeleteNotificationInfoResponse(
                    notificationId = history.pk,
                    notificationType = history.notificationType,
                    createTime = history.createdAt
                )
        }
    }

    data class BlockedNotificationInfoResponse(
        override val notificationId: Long,
        override val notificationType: NotificationType,
        override val createTime: LocalDateTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val blockExpirationDateTime: LocalDateTime
    ) : CommonNotificationInfo(notificationId, notificationType, createTime) {

        companion object {
            fun of(history: NotificationHistory): BlockedNotificationInfoResponse =
                BlockedNotificationInfoResponse(
                    notificationId = history.pk,
                    notificationType = history.notificationType,
                    createTime = history.createdAt,
                    blockExpirationDateTime = history.toMember.untilBan
                )
        }
    }
}
