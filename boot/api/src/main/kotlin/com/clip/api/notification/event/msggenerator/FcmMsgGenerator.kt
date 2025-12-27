package com.clip.api.notification.event.msggenerator

import com.clip.api.notification.event.FCMEvent
import com.google.firebase.messaging.Message

interface FcmMsgGenerator {
    companion object {
        const val TITLE = "sooum"
    }

    fun generateMsg(fcmEvent: FCMEvent): Message

    fun isSupported(fcmEvent: FCMEvent): Boolean
}