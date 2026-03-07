package com.clip.api.notification.event

import com.clip.api.notification.event.fcmsender.FcmSender
import com.clip.api.notification.event.msggenerator.FcmMsgStrategyRegistry
import com.clip.data.notification.entity.notificationtype.NotificationType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class FcmEventListener(
    private val fcmSender: FcmSender,
    private val fcmMsgStrategyRegistry: FcmMsgStrategyRegistry
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    fun sendFcm(
        fcmEvent: FCMEvent
    ) {
        val message = fcmMsgStrategyRegistry.getMessage(fcmEvent)
        fcmSender.send(message)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    fun sendMulticastTagNotification(
        multiFcmTagEvent: MultiFcmTagEvent
    ) {
        multiFcmTagEvent.notificationHistories.forEach { notificationHistory ->
            val message = fcmMsgStrategyRegistry.getMessage(TagUsageFcmEvent(
                multiFcmTagEvent.tagContent,
                multiFcmTagEvent.targetCardId,
                notificationHistory.pk,
                notificationHistory.toMember.deviceType,
                notificationHistory.toMember.firebaseToken,
                notificationHistory.notificationType
            ))
            fcmSender.send(message)
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    fun sendMulticastFollowerCardUploadNotification(
        multiFcmTagEvent: MultiFcmFollowerCardUploadEvent
    ) {
        multiFcmTagEvent.recipientList.forEach { recipient ->
            val message = fcmMsgStrategyRegistry.getMessage(FollowerCardUploadFCMEvent(
                multiFcmTagEvent.targetCardId,
                multiFcmTagEvent.content,
                multiFcmTagEvent.nickname,
                multiFcmTagEvent.userImgUrl,
                recipient.deviceType,
                recipient.firebaseToken,
                NotificationType.FOLLOWER_CARD_UPLOAD
            ))
            fcmSender.send(message)
        }
    }
}