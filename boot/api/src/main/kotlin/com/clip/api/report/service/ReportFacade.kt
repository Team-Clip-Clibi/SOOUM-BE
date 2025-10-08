package com.clip.api.report.service

import com.clip.data.card.entity.parenttype.CardType
import com.clip.data.card.service.CommentCardService
import com.clip.data.member.service.MemberService
import com.clip.data.report.entity.reporttype.ReportType
import com.clip.global.exception.IllegalStateException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportFacade(
    private val feedReportUseCase: FeedReportUseCase,
    private val commentReportUseCase: CommentReportUseCase,
    private val memberService: MemberService,
    private val commentCardService: CommentCardService,
) {

    @Transactional
    fun createCardReport(cardId: Long, reportType: ReportType, userId: Long) {
        hasAlreadyReported(cardId, userId)

        val member = memberService.findMember(userId)
        val cardType = commentCardService.findCardType(cardId)

        when (cardType) {
            CardType.FEED_CARD -> feedReportUseCase.saveReport(cardId, reportType, member)
            CardType.COMMENT_CARD -> commentReportUseCase.saveReport(cardId, reportType, member)
        }
    }

    private fun hasAlreadyReported(cardId: Long, userId: Long) {
        if (feedReportUseCase.hasAlreadyReported(cardId, userId) || commentReportUseCase.hasAlreadyReported(cardId, userId))
            throw IllegalStateException.IllegalStatementException("이미 처리된 신고입니다.")
    }

}