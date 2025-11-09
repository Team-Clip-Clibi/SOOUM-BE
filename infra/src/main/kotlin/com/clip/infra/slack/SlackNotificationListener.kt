package com.clip.infra.slack

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SlackNotificationListener(
    private val slackService: SlackService
) {
    private val logger = LoggerFactory.getLogger(SlackNotificationListener::class.java)

    @Async
    @EventListener
    fun sendSlackNotification(event: SlackNotificationEvent) {
        try {
            slackService.sendSlackMessage(
                message = event.message,
                exception = event.exception,
                requestInfo = event.requestInfo,
                userId = event.userId,
                customFields = event.customFields
            )
        } catch (e: Exception) {
            logger.error("Failed to send Slack notification", e)
        }
    }
}
