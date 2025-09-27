package com.clip.api.notification.event

import com.clip.api.notification.service.FCMService
import com.clip.data.notification.entity.notificationtype.NotificationType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class FCMEventListener(
    private val fcmService: FCMService,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    fun sendSystemFCM(
        systemFCMEvent: SystemFCMEvent,
    ){
        when (systemFCMEvent.notificationType) {
            NotificationType.BLOCKED,
            NotificationType.DELETED,
            NotificationType.TRANSFER_SUCCESS -> fcmService.sendSystemFCM(systemFCMEvent)
            else -> IllegalArgumentException("notification type ${systemFCMEvent.notificationType} not supported.")
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    fun sendNotificationFCM(
        notificationFCMEvent: NotificationFCMEvent
    ) {
        when (notificationFCMEvent.notificationType) {
            NotificationType.FEED_LIKE,
            NotificationType.COMMENT_LIKE,
            NotificationType.COMMENT_WRITE,
            NotificationType.FOLLOW -> fcmService.sendNotificationFCM(notificationFCMEvent)
            else -> IllegalArgumentException("notification type ${notificationFCMEvent.notificationType} not supported.")
        }
    }

}