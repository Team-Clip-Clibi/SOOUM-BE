package com.clip.api.report.service

import com.clip.api.card.service.CardUseCase
import com.clip.api.notification.event.SystemFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.service.FeedCardService
import com.clip.data.member.entity.Member
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.data.report.entity.reporttype.ReportType
import com.clip.data.report.service.FeedReportService
import com.clip.global.exception.IllegalArgumentException
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedReportUseCase(
    private val eventPublisher: ApplicationEventPublisher,
    private val notificationUseCase: NotificationUseCase,
    private val feedReportService: FeedReportService,
    private val feedCardService: FeedCardService,
    private val cardUseCase: CardUseCase,
) {
    companion object {
        private const val REPORT_LIMIT = 7
    }

    fun hasAlreadyReported(cardId: Long, userId: Long): Boolean =
        feedReportService.hasAlreadyReported(cardId, userId)

    @Transactional
    fun saveReport(cardId: Long, reportType: ReportType, requester: Member) {
        runCatching {
            feedCardService.findFeedCard(cardId)
        }.onFailure { ex ->
            when (ex) {
                is EntityNotFoundException -> throw IllegalArgumentException.CardNotFoundException()
                else -> throw ex
            }
        }.onSuccess { feedCard ->
            feedReportService.save(requester, feedCard, reportType)
            deleteFeedCardIfReportLimitExceeded(feedCard)
        }
    }

    private fun deleteFeedCardIfReportLimitExceeded(feedCard: FeedCard) {
        val feedReports = feedReportService.findFeedReport(feedCard.pk)

        if (feedReports.size > REPORT_LIMIT) {
            deleteFeedAndAssociationsByReport(feedCard)
            writeBan(feedCard.writer)
        }
    }

    private fun deleteFeedAndAssociationsByReport(feedCard: FeedCard) {
        val writer = feedCard.writer

        cardUseCase.deleteFeedCardWithRelations(feedCard)
        val notificationHistory = notificationUseCase.saveCardDeletedHistoryByReport(writer.pk)

        if (writer.isAllowNotify) {
            eventPublisher.publishEvent(
                SystemFCMEvent(
                    null,
                    notificationHistory.pk,
                    writer.deviceType,
                    writer.firebaseToken,
                    NotificationType.DELETED
                )
            )
        }
    }

    private fun writeBan(member: Member) {
        notificationUseCase.saveBlockedHistoryAndDeletePreviousHistories(member.pk)
        member.ban()

        if (member.isAllowNotify) {
            eventPublisher.publishEvent(
                SystemFCMEvent(
                    member.untilBan,
                    0L,
                    member.deviceType,
                    member.firebaseToken,
                    NotificationType.BLOCKED
                )
            )
        }
    }

}