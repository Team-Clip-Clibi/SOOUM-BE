package com.clip.api.card.service

import com.clip.api.notification.event.CardFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.card.entity.*
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.CommentLikeService
import com.clip.data.card.service.FeedCardService
import com.clip.data.card.service.FeedLikeService
import com.clip.data.member.entity.Member
import com.clip.data.member.service.MemberService
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.global.exception.IllegalArgumentException.ParameterNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.OptimisticLockException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CardLikeUseCase(
    private val memberService: MemberService,
    private val feedLikeService: FeedLikeService,
    private val commentLikeService: CommentLikeService,
    private val notificationUseCase: NotificationUseCase,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val feedCardService: FeedCardService,
    private val commentCardService: CommentCardService,
) {
    private val logger = KotlinLogging.logger {}

    @Transactional
    fun createCardLike(cardId: Long, userId: Long) {
        val likedMember = memberService.findMember(userId)

        when (val card = getCard(cardId)) {
            is FeedCard -> {
                val existingLike = feedLikeService.findFeedLikedOp(card.pk, likedMember.pk)

                existingLike.orElse(null)?.let { like ->
                    if (!like.isDeleted) return

                    like.create()
                    return
                }

                try {
                    feedLikeService.save(FeedLike(card, likedMember))
                    sendNotification(card, likedMember)
                } catch (e: DataIntegrityViolationException) {
                    logger.debug(e) { "Duplicate like attempt detected for card=${card.pk}, user=${likedMember.pk}" }
                }
            }

            is CommentCard -> {
                val existingLike = commentLikeService.findCommentLikedOp(card.pk, likedMember.pk)

                existingLike.orElse(null)?.let { like ->
                    if (!like.isDeleted) return

                    like.create()
                    return
                }

                try {
                    commentLikeService.save(CommentLike(card, likedMember))
                    sendNotification(card, likedMember)
                } catch (e: DataIntegrityViolationException) {
                    logger.debug(e) { "Duplicate like attempt detected for card=${card.pk}, user=${likedMember.pk}" }
                }
            }

            else -> throw IllegalArgumentException("지원하지 않는 카드 타입입니다.")
        }
    }

    private fun sendNotification(card: Card, member: Member) {
        if (card.writer.pk == member.pk) return

        val (notificationPk, notificationType) = when (card) {
            is FeedCard -> notificationUseCase.saveFeedLikeHistory(member, card).pk to NotificationType.FEED_LIKE
            is CommentCard -> notificationUseCase.saveCommentLikeHistory(
                member,
                card
            ).pk to NotificationType.COMMENT_LIKE

            else -> return
        }

        if (card.writer.isAllowNotify) {
            applicationEventPublisher.publishEvent(
                CardFCMEvent(
                    member.nickname,
                    card.pk,
                    notificationPk,
                    card.writer.deviceType,
                    card.writer.firebaseToken,
                    notificationType
                )
            )
        }
    }

    @Transactional
    fun deleteCardLike(cardId: Long, userId: Long) {
        val member = memberService.findMember(userId)

        when (val card = getCard(cardId)) {
            is FeedCard -> {
                try {
                    feedLikeService.findFeedLikedOp(card.pk, member.pk)
                        .orElse(null)?.takeUnless { it.isDeleted }?.delete()
                } catch (e: OptimisticLockException) {
                    logger.debug(e) { "Concurrent delete detected for card=${card.pk}, user=${member.pk}" }
                }
            }

            is CommentCard -> {
                try {
                    commentLikeService.findCommentLikedOp(card.pk, member.pk)
                        .orElse(null)?.takeUnless { it.isDeleted }?.delete()
                } catch (e: OptimisticLockException) {
                    logger.debug(e) { "Concurrent delete detected for card=${card.pk}, user=${member.pk}" }
                }
            }

            else -> throw IllegalArgumentException("지원하지 않는 카드 타입입니다.")
        }
    }

    private fun getCard(cardId: Long): Card =
        when {
            feedCardService.isExistFeedCard(cardId) -> feedCardService.findFeedCard(cardId)
            commentCardService.isExistCommentCard(cardId) -> commentCardService.findCommentCard(cardId)
            else -> throw ParameterNotFoundException("카드(id: $cardId)를 찾을 수 없습니다.", cardId)
        }
}

