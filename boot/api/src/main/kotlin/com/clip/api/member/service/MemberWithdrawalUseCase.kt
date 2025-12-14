package com.clip.api.member.service

import com.clip.api.member.controller.dto.WithdrawalRequest
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.service.*
import com.clip.data.follow.service.FollowService
import com.clip.data.img.service.CardImgService
import com.clip.data.img.service.ProfileImgService
import com.clip.data.member.entity.*
import com.clip.data.member.service.*
import com.clip.data.notification.service.NotificationHistoryService
import com.clip.data.tag.service.CommentTagService
import com.clip.data.tag.service.FavoriteTagService
import com.clip.data.tag.service.FeedTagService
import com.clip.data.visitor.service.VisitorService
import com.clip.global.exception.TokenException
import com.clip.global.security.jwt.JwtProvider
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MemberWithdrawalUseCase(
    private val memberService: MemberService,
    private val suspendedService: SuspendedService,
    private val jwtProvider: JwtProvider,
    private val blacklistService: BlacklistService,
    private val feedCardService: FeedCardService,
    private val commentCardService: CommentCardService,
    private val feedLikeService: FeedLikeService,
    private val commentLikeService: CommentLikeService,
    private val favoriteTagService: FavoriteTagService,
    private val popularFeedService: PopularFeedService,
    private val cardImgService: CardImgService,
    private val followService: FollowService,
    private val visitorService: VisitorService,
    private val blockMemberService: BlockMemberService,
    private val policyService: PolicyService,
    private val refreshTokenService: RefreshTokenService,
    private val profileImgService: ProfileImgService,
    private val accountTransferService: AccountTransferService,
    private val feedTagService: FeedTagService,
    private val commentTagService: CommentTagService,
    private val notificationHistoryService: NotificationHistoryService,
    private val memberWithdrawalReasonService: MemberWithdrawalReasonService,
    private val accountTransferHistoryService: AccountTransferHistoryService,
    private val entityManager: EntityManager
) {

    @Transactional
    fun withdrawMember(memberPk: Long, withdrawalRequest: WithdrawalRequest) {
        if (blacklistService.findByToken(withdrawalRequest.refreshToken).isPresent)
            throw TokenException.BlacklistTokenException(token = withdrawalRequest.refreshToken)

        val member = memberService.findMember(memberPk)
        handleSuspendedUser(member)

        addTokensToBlacklist(withdrawalRequest)

        memberWithdrawalReasonService.save(
            MemberWithdrawalReason(
                withdrawalRequest.reason,
            )
        )

        executeWithdrawal(memberPk)
    }

    @Transactional
    fun withdrawMember(memberPk: Long) {
        val refreshToken = refreshTokenService.findRefreshToken(memberPk)
        blacklistService.save(
            Blacklist(
                refreshToken,
                jwtProvider.getTokenExpiration(refreshToken)
            )
        )
        executeWithdrawal(memberPk)
    }

    private fun executeWithdrawal(memberPk: Long) {
        popularFeedService.deletePopularCardByMemberPk(memberPk)

        cardImgService.updateCardImgNull(memberPk)

        feedLikeService.deleteAllMemberLikes(memberPk)
        commentLikeService.deleteAllMemberLikes(memberPk)

        favoriteTagService.deleteAllFavoriteTag(memberPk)

        feedTagService.deleteFeedTagByMemberPk(memberPk)
        commentTagService.deleteCommentTag(memberPk)

        commentCardService.deleteCommentCardByMemberPk(memberPk)
        feedCardService.deleteFeedCardByMemberPk(memberPk)

        followService.deleteAllFollow(memberPk)
        visitorService.handleVisitorOnMemberWithdraw(memberPk)
        blockMemberService.deleteAllBlockMember(memberPk)

        policyService.deletePolicyTerm(memberPk)
        refreshTokenService.deleteRefreshToken(memberPk)
        accountTransferService.deleteAccountTransfer(memberPk)
        profileImgService.updateProfileImgNull(memberPk)
        notificationHistoryService.deleteAllNotificationHistory(memberPk)
        accountTransferHistoryService.deleteAccountTransferHistory(memberPk)

        memberService.deleteMember(memberPk)
        entityManager.flush()
    }


    private fun handleSuspendedUser(member: Member) {
        val threshold = LocalDateTime.now().plusDays(7)
        val (untilBan, isBanUser) = if (member.role == Role.BANNED && member.untilBan != null && member.untilBan > threshold) {
            member.untilBan to true
        } else {
            threshold to false
        }

        val suspended = Suspended.builder()
            .deviceId(member.deviceId)
            .untilBan(untilBan)
            .isBanUser(isBanUser)
            .build()

        suspendedService.save(suspended)
    }


    private fun addTokensToBlacklist(withdrawalRequest: WithdrawalRequest) {
        blacklistService.save(
            Blacklist(
                withdrawalRequest.accessToken,
                jwtProvider.getTokenExpiration(withdrawalRequest.accessToken)
            )
        )
        blacklistService.save(
            Blacklist(
                withdrawalRequest.refreshToken,
                jwtProvider.getTokenExpiration(withdrawalRequest.refreshToken)
            )
        )
    }
}
