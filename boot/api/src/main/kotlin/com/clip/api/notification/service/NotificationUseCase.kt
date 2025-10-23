package com.clip.api.notification.service

import com.clip.api.notification.controller.dto.NotificationDto
import com.clip.data.card.entity.Card
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.member.entity.Member
import com.clip.data.member.service.MemberService
import com.clip.data.notification.entity.NotificationHistory
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.data.notification.service.NotificationHistoryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationUseCase(
    private val notificationHistoryService: NotificationHistoryService,
    private val memberService: MemberService
) {
    fun updateNotificationStatusToRead(notificationId: Long, userId: Long) {
        if (isNotificationOwner(userId, notificationId)) {
            throw IllegalArgumentException("notificationId: $notificationId, userId: $userId 본인의 알림이 아닙니다.")
        }
        notificationHistoryService.updateToRead(notificationId)
    }

    private fun isNotificationOwner(notificationId: Long, userId: Long): Boolean {
        return notificationHistoryService.findToMember(notificationId).pk.equals(userId)
    }

    fun findUnreadNotifications(lastId: Long?, userId: Long): List<NotificationDto.CommonNotificationInfo> =
        notificationHistoryService.findUnreadNotifications(Optional.ofNullable(lastId), userId)
            .map { notification ->
                when {
                    isDeleteNotification(notification) -> NotificationDto.DeleteNotificationInfoResponse.of(notification)
                    isBlockedNotification(notification) -> NotificationDto.BlockedNotificationInfoResponse.of(notification)

                    else -> NotificationDto.NotificationInfoResponse.of(notification)
                }
            }.toList()

    fun findReadNotifications(lastId: Long?, userId: Long): List<NotificationDto.CommonNotificationInfo> =
        notificationHistoryService.findReadNotifications(Optional.ofNullable(lastId), userId)
            .map { notification ->
                when {
                    isDeleteNotification(notification) -> NotificationDto.DeleteNotificationInfoResponse.of(notification)
                    isBlockedNotification(notification) -> NotificationDto.BlockedNotificationInfoResponse.of(notification)
                    isFollowNotification(notification) -> NotificationDto.FollowNotificationInfoResponse.of(notification)
                    else -> NotificationDto.NotificationInfoResponse.of(notification)
                }
            }.toList()

    fun saveCommentCardHistory(fromMemberId: Long, targetCardId: Long, parentCard: Card): NotificationHistory {
        val fromMember = memberService.findMember(fromMemberId)
        return notificationHistoryService.save(NotificationHistory.ofCommentWrite(fromMember, targetCardId, parentCard))
    }


    private fun isDeleteNotification(notification: NotificationHistory) =
        notification.notificationType.equals(NotificationType.DELETED)

    private fun isBlockedNotification(notification: NotificationHistory) =
        notification.notificationType.equals(NotificationType.BLOCKED)

    private fun isFollowNotification(notification: NotificationHistory) =
        notification.notificationType.equals(NotificationType.FOLLOW)

    @Transactional
     fun saveCardDeletedHistoryByReport(toMemberId: Long): NotificationHistory {
         val member = memberService.findMember(toMemberId)
         return notificationHistoryService.save(
             NotificationHistory.ofDeleted(member)
         )
     }

    @Transactional
    fun saveBlockedHistoryAndDeletePreviousHistories(toMemberId: Long): NotificationHistory {
        notificationHistoryService.deletePreviousBlockedHistories(toMemberId)
        val member = memberService.findMember(toMemberId)
        return notificationHistoryService.save(
            NotificationHistory.ofBlocked(member)
        )
    }

    @Transactional
    fun saveFeedLikeHistory(fromMember: Member, feedCard: FeedCard): NotificationHistory {
        return notificationHistoryService.save(
            NotificationHistory.ofFeedLike(fromMember, feedCard)
        )
    }

    @Transactional
    fun saveCommentLikeHistory(fromMember: Member, commentCard: CommentCard): NotificationHistory {
        return notificationHistoryService.save(
            NotificationHistory.ofCommentLike(fromMember, commentCard)
        )
    }

    @Transactional
    fun saveFollowHistory(fromMember: Member, toMember: Member): NotificationHistory {
        return notificationHistoryService.save(
            NotificationHistory.ofFollow(fromMember, toMember)
        )
    }

    fun findNotificationHistoryByMemberAndType(fromMemberId: Long, toMemberId: Long, notificationType: NotificationType): NotificationHistory? {
        return notificationHistoryService.findNotificationHistoryByMemberAndType(fromMemberId, toMemberId, notificationType)
    }

    @Transactional
    fun deleteNotificationHistory(notificationHistory: NotificationHistory) {
        notificationHistoryService.delete(notificationHistory)
    }
}