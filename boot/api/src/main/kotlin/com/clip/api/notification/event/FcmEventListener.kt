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
            if (notificationHistory.toMember.firebaseToken.isNullOrBlank()) return@forEach
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
            if (recipient.firebaseToken.isNullOrBlank()) return@forEach
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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    fun sendMulticastArticleCardUploadNotification(
        multiFcmArticleEvent: MultiFcmArticleCardUploadEvent
    ) {
        multiFcmArticleEvent.recipientList.forEach { recipient ->
            if (recipient.firebaseToken.isNullOrBlank()) return@forEach
            val message = fcmMsgStrategyRegistry.getMessage(ArticleCardUploadFCMEvent(
                multiFcmArticleEvent.targetCardId,
                multiFcmArticleEvent.content,
                multiFcmArticleEvent.userImgUrl,
                recipient.deviceType,
                recipient.firebaseToken,
                NotificationType.FOLLOWER_CARD_UPLOAD
            ))
            fcmSender.send(message)
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    fun sendMulticastFeedCardCommentViewersNotification(
        multiFcmEvent: MultiFcmFeedCardCommentViewersEvent
    ) {
        multiFcmEvent.recipientList.forEach { recipient ->
            val message = fcmMsgStrategyRegistry.getMessage(
                FeedCommentViewedCardFCMEvent(
                    multiFcmEvent.targetCardId,
                    multiFcmEvent.commentContent,
                    recipient.deviceType,
                    recipient.firebaseToken,
                    NotificationType.VIEWED_FEED_COMMENT_WRITE
                )
            )
            fcmSender.send(message)
        }
    }
}
