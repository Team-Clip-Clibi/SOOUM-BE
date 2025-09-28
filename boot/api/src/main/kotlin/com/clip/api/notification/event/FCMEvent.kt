package com.clip.api.notification.event

import com.clip.data.member.entity.DeviceType
import com.clip.data.notification.entity.notificationtype.NotificationType

sealed class FCMEvent(
    open val notificationId: Long,
    open val deviceType: DeviceType,
    open val fcmToken: String,
    open val notificationType: NotificationType
)

class SystemFCMEvent(
    notificationId: Long,
    targetDeviceType: DeviceType,
    fcmToken: String,
    notificationType: NotificationType
): FCMEvent(notificationId, targetDeviceType, fcmToken, notificationType)

class NotificationFCMEvent(
    val publisherNickname: String,
    val targetCardId: Long,
    notificationId: Long,
    targetDeviceType: DeviceType,
    fcmToken: String,
    notificationType: NotificationType
): FCMEvent(notificationId, targetDeviceType, fcmToken, notificationType)