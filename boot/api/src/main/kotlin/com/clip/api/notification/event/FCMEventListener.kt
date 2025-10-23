package com.clip.api.notification.event

import com.clip.api.notification.event.fcmsender.FcmSender
import com.clip.api.notification.event.msggenerator.FcmMsgStrategyRegistry
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class FCMEventListener(
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
}