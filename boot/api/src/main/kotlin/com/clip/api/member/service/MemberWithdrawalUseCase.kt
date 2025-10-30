package com.clip.api.member.service

import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.service.*
import com.clip.data.follow.service.FollowService
import com.clip.data.img.service.CardImgService
import com.clip.data.img.service.ProfileImgService
import com.clip.data.member.entity.Blacklist
import com.clip.data.member.entity.Member
import com.clip.data.member.entity.Role
import com.clip.data.member.entity.Suspended
import com.clip.data.member.service.*
import com.clip.data.notification.service.NotificationHistoryService
import com.clip.data.tag.service.CommentTagService
import com.clip.data.tag.service.FavoriteTagService
import com.clip.data.tag.service.FeedTagService
import com.clip.data.visitor.service.VisitorService
import com.clip.global.exception.TokenException
import com.clip.global.security.jwt.JwtProvider
import com.clip.global.security.jwt.TokenDto
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
    private val notificationHistoryService: NotificationHistoryService
) {

    @Transactional
    fun withdrawMember(memberPk: Long, withdrawalRequest: TokenDto) {
        if (blacklistService.findByToken(withdrawalRequest.refreshToken).isPresent)
            throw TokenException.BlacklistTokenException(token = withdrawalRequest.refreshToken)

        val member = memberService.findMember(memberPk)
        handleSuspendedUser(member)

        addTokensToBlacklist(withdrawalRequest)

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

        feedTagService.deleteFeedTag(memberPk)
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

        memberService.deleteMember(memberPk)
    }

    private fun handleSuspendedUser(member: Member) {
        val (untilBan, isBanUser) = when {
            member.role == Role.BANNED && member.untilBan > LocalDateTime.now().plusDays(7) ->
                member.untilBan to true
            else ->
                LocalDateTime.now().plusDays(7) to false
        }

        val suspended = Suspended.builder()
            .deviceId(member.deviceId)
            .untilBan(untilBan)
            .isBanUser(isBanUser)
            .build()

        suspendedService.save(suspended)
    }


    private fun addTokensToBlacklist(withdrawalRequest: TokenDto) {
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
