package com.clip.api.notification.event

import com.clip.data.notification.entity.NotificationHistory

data class MultiFcmTagEvent(
    val targetCardId: Long,
    val tagContent: String,
    val notificationHistories: List<NotificationHistory>
)
