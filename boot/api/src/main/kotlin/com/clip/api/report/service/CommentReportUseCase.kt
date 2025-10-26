package com.clip.api.report.service

import com.clip.api.card.service.CardUseCase
import com.clip.api.notification.event.SystemFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.service.CommentCardService
import com.clip.data.member.entity.Member
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.data.report.entity.reporttype.ReportType
import com.clip.data.report.service.CommentReportService
import com.clip.global.exception.IllegalArgumentException
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentReportUseCase(
    private val eventPublisher: ApplicationEventPublisher,
    private val commentReportService: CommentReportService,
    private val commentCardService: CommentCardService,
    private val cardUseCase: CardUseCase,
    private val notificationUseCase: NotificationUseCase,
) {
    companion object {
        private const val REPORT_LIMIT = 7
    }

    fun hasAlreadyReported(cardId: Long, userId: Long): Boolean =
        commentReportService.hasAlreadyReported(cardId, userId)

    @Transactional
    fun saveReport(cardId: Long, reportType: ReportType, requester: Member) {
        runCatching {
            commentCardService.findCommentCard(cardId)
        }.onFailure { ex ->
            when(ex) {
                is EntityNotFoundException -> throw IllegalArgumentException.CardNotFoundException()
                else -> throw ex
            }
        }.onSuccess { commentCard ->
            commentReportService.save(requester, commentCard, reportType)
            deleteCommentCardIfReportLimitExceeded(commentCard)
        }
    }

    private fun deleteCommentCardIfReportLimitExceeded(commentCard: CommentCard) {
        val commentReports = commentReportService.findCommentReports(commentCard.pk)
        if (commentReports.size > REPORT_LIMIT) {
            deleteCommentAndAssociationsByReport(commentCard)
            writeBan(commentCard.writer)
        }
    }

    private fun deleteCommentAndAssociationsByReport(commentCard: CommentCard) {
        val writer = commentCard.writer
        cardUseCase.deleteCommentCardWithRelations(commentCard)
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
                    member.pk,
                    member.deviceType,
                    member.firebaseToken,
                    NotificationType.BLOCKED
                )
            )
        }
    }
}