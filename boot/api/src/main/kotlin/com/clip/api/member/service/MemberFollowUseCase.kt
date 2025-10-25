package com.clip.api.member.service

import com.clip.api.member.controller.dto.FollowCountDto
import com.clip.api.member.controller.dto.FollowDto
import com.clip.api.member.controller.dto.FollowInfoDto
import com.clip.api.notification.event.FollowFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.block.service.BlockMemberService
import com.clip.data.follow.service.FollowService
import com.clip.data.member.service.MemberService
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.infra.s3.S3ImgService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class MemberFollowUseCase(
    private val memberService: MemberService,
    private val followService: FollowService,
    private val notificationUseCase: NotificationUseCase,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val blockMemberService: BlockMemberService,
    private val imgService: S3ImgService
) {

    fun followMember(followRequest: FollowDto, userId: Long) {
        val fromMember = memberService.findMember(userId)
        val toMember = memberService.findMember(followRequest.userId)

        if (followService.isAlreadyFollowing(fromMember, toMember)) {
            throw IllegalStateException("이미 팔로우한 회원입니다.")
        }

        notificationUseCase.findNotificationHistoryByMemberAndType(fromMember.pk, toMember.pk, NotificationType.FOLLOW)?.let {
            if( it.createdAt.isBefore( LocalDateTime.now().minusHours(1) ) ) {
                notificationUseCase.deleteNotificationHistory(it)
                val notificationHistory = notificationUseCase.saveFollowHistory(fromMember, toMember)
                applicationEventPublisher.publishEvent(
                    FollowFCMEvent(
                        fromMember.nickname,
                        fromMember.pk,
                        notificationHistory.pk,
                        toMember.deviceType,
                        toMember.firebaseToken,
                        NotificationType.FOLLOW
                    )
                )

            }
        }
        followService.saveFollower(fromMember, toMember)

    }

    fun unfollowMember(toMemberId: Long, userId: Long) {
        val fromMember = memberService.findMember(userId)
        val toMember = memberService.findMember(toMemberId)

        followService.deleteFollower(fromMember, toMember)
    }

    fun getFollowingList(profileOwnerId: Long, userId: Long, lastId: Long?) : List<FollowInfoDto> {
        val blockedMemberIds = blockMemberService.findAllBlockMemberPks(userId)
        val followings = followService.findFollowingWithoutBlockedMembers(Optional.ofNullable(lastId), profileOwnerId, blockedMemberIds)
        val followedFollowingsPk = followService.findFollowedFollowingsPk(userId, followings)

        return followings.map {
            FollowInfoDto(
                memberId = it.pk,
                nickname = it.nickname,
                profileImageUrl = imgService.generateProfileImgUrl(it.profileImgName ?: ""),
                isFollowing = followedFollowingsPk.contains(it.pk),
                isRequester = it.pk == userId
            )
        }
    }

    fun getFollowerList(profileOwnerId: Long, userId: Long, lastId: Long?) : List<FollowInfoDto> {
        val blockedMemberIds = blockMemberService.findAllBlockMemberPks(userId)
        val followers = followService.findFollowerWithoutBlockedMembers(Optional.ofNullable(lastId), profileOwnerId, blockedMemberIds)
        val followedFollowersPk = followService.findFollowedFollowersPk(userId, followers)

        return followers.map {
            FollowInfoDto(
                memberId = it.pk,
                nickname = it.nickname,
                profileImageUrl = imgService.generateProfileImgUrl(it.profileImgName ?: ""),
                isFollowing = followedFollowersPk.contains(it.pk),
                isRequester = it.pk == userId
            )
        }
    }

}