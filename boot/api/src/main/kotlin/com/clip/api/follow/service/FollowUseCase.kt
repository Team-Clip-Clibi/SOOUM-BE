package com.clip.api.follow.service

import com.clip.api.follow.controller.dto.FollowDto
import com.clip.api.notification.event.FollowFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.follow.service.FollowService
import com.clip.data.member.service.MemberService
import com.clip.data.notification.entity.notificationtype.NotificationType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FollowUseCase(
    private val memberService: MemberService,
    private val followService: FollowService,
    private val notificationUseCase: NotificationUseCase,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    fun followMember(followRequest: FollowDto, userId: Long) {
        val fromMember = memberService.findMember(followRequest.userId)
        val toMember = memberService.findMember(userId)

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

    fun unfollowMember(followRequest: FollowDto, userId: Long) {
        val fromMember = memberService.findMember(followRequest.userId)
        val toMember = memberService.findMember(userId)

        followService.deleteFollower(fromMember, toMember)
    }
}