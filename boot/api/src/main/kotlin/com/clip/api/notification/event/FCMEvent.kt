package com.clip.api.notification.event

import com.clip.data.member.entity.DeviceType
import com.clip.data.notification.entity.notificationtype.NotificationType
import java.time.LocalDateTime

open class FCMEvent(
    val notificationId: Long,
    val deviceType: DeviceType,
    val fcmToken: String,
    val notificationType: NotificationType
)

class SystemFCMEvent(
    val expiredAt: LocalDateTime?,
    notificationId: Long,
    targetDeviceType: DeviceType,
    fcmToken: String,
    notificationType: NotificationType
): FCMEvent(notificationId, targetDeviceType, fcmToken, notificationType)

class CardFCMEvent(
    val writerNickname: String,
    val targetCardId: Long,
    notificationId: Long,
    targetDeviceType: DeviceType,
    fcmToken: String,
    notificationType: NotificationType
): FCMEvent(notificationId, targetDeviceType, fcmToken, notificationType)

class FollowFCMEvent(
    val followingNickname: String,
    val followingMemberId: Long,
    notificationId: Long,
    targetDeviceType: DeviceType,
    fcmToken: String,
    notificationType: NotificationType
): FCMEvent(notificationId, targetDeviceType, fcmToken, notificationType)