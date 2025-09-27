package com.clip.api.card.service

import com.clip.api.card.controller.dto.CreateCommentCardRequest
import com.clip.api.card.controller.dto.CreateFeedCardRequest
import com.clip.api.card.mapper.CardMapper
import com.clip.api.notification.event.NotificationFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.card.entity.Card
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.card.entity.parenttype.CardType
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedCardService
import com.clip.data.img.service.CardImgService
import com.clip.data.member.entity.Member
import com.clip.data.member.entity.Role
import com.clip.data.member.service.MemberService
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.data.tag.service.CommentTagService
import com.clip.data.tag.service.FeedTagService
import com.clip.data.tag.service.TagService
import com.clip.global.exception.IllegalArgumentException.ParameterNotFoundException
import com.clip.global.exception.ImageException
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3ImgService
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class CardUseCase(
    private val rekognitionService: RekognitionService,
    private val s3ImgService: S3ImgService,
    private val cardMapper: CardMapper,
    private val cardImgService: CardImgService,
    private val memberService: MemberService,
    private val tagService: TagService,
    private val feedTagService: FeedTagService,
    private val commentTagService: CommentTagService,
    private val feedCardService: FeedCardService,
    private val commentCardService: CommentCardService,
    private val notificationUseCase: NotificationUseCase,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    companion object {
        fun getIp(request: HttpServletRequest): String =
            request.getHeader("X-Forwarded-For")
                ?.takeUnless { it.isEmpty() || it.equals("unknown", ignoreCase = true) }
                ?: request.remoteAddr
    }

    @Transactional
    fun createFeedCard(
        httpServletRequest: HttpServletRequest,
        createFeedCardRequest: CreateFeedCardRequest,
        userId: Long
    ) {
        val member = memberService.findMember(userId)

        if (isBanPeriodExpired(member))
            member.unban()

        if (isUserImgType(createFeedCardRequest.imgType))
            validUserCardImg(createFeedCardRequest.imgName)

        val feedCard = feedCardService.saveFeedCard(
            cardMapper.toFeedCard(
                createFeedCardRequest,
                getIp(httpServletRequest),
                member
            )
        )
        if (isUserImgType(CardImgType.USER))
            cardImgService.updateCardImg(feedCard, createFeedCardRequest.imgName)

        val savedTags = tagService.saveAllAndIncrementTagCnt(createFeedCardRequest.tags)
        feedTagService.saveAll(feedCard, savedTags)
    }

    fun createCommentCard(
        httpServletRequest: HttpServletRequest,
        createCommentCardRequest: CreateCommentCardRequest,
        cardId: Long,
        userId: Long
    ) {
        val member = memberService.findMember(userId)

        if (isBanPeriodExpired(member))
            member.unban()

        if (isUserImgType(createCommentCardRequest.imgType))
            validUserCardImg(createCommentCardRequest.imgName)

        val parentCard = getParentCard(cardId)
        val parentCardType = getCardType(parentCard)
        val masterCardId = getMasterCardId(parentCard)

        val commentCard = commentCardService.saveComment(
            cardMapper.toCommentCard(
                createCommentCardRequest,
                member,
                parentCardType,
                parentCard.pk,
                masterCardId,
                getIp(httpServletRequest)
            )
        )

        if (isUserImgType(CardImgType.USER))
            cardImgService.updateCardImg(commentCard, createCommentCardRequest.imgName)

        val savedTags = tagService.saveAllAndNoIncrementTagCnt(createCommentCardRequest.tags)
        commentTagService.saveAll(commentCard, savedTags)

        if (!parentCard.isWriter(userId)) {
            val commentWriteNotification = notificationUseCase.saveCommentCardHistory(userId, commentCard.pk, parentCard)
            if (parentCard.writer.isAllowNotify)
                applicationEventPublisher.publishEvent(
                    NotificationFCMEvent(
                        member.nickname,
                        commentCard.pk,
                        commentWriteNotification.pk,
                        parentCard.writer.deviceType,
                        parentCard.writer.firebaseToken,
                        NotificationType.COMMENT_WRITE
                    )
                )
        }
    }

    private fun getMasterCardId(card: Card): Long {
        return when (card) {
            is FeedCard -> card.pk
            is CommentCard -> card.masterCard
            else -> throw IllegalArgumentException()
        }
    }

    private fun getCardType(card: Card): CardType =
        if (card is FeedCard) CardType.FEED_CARD else CardType.COMMENT_CARD


    private fun getParentCard(cardId: Long): Card {
        if (feedCardService.isExistFeedCard(cardId))
            return feedCardService.findFeedCard(cardId)
        else if (commentCardService.isExistCommentCard(cardId))
            return commentCardService.findCommentCard(cardId)
        else throw ParameterNotFoundException("$cardId 에 해당하는 카드를 찾지 못해 답카드를 작성할 수 없습니다.",cardId)

    }

    private fun isBanPeriodExpired(member: Member): Boolean =
        member.untilBan.isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) && member.role.equals(Role.BANNED)

    private fun isUserImgType(cardImgType: CardImgType) =
        cardImgType == CardImgType.USER

    private fun validUserCardImg(imgName: String) {
        if (!s3ImgService.isCardImgSaved(imgName))
            throw ImageException.ImageNotFoundException(imgName = imgName)

        if (!rekognitionService.isModeratingCardImg(imgName))
            throw ImageException.InvalidImageException(imgName = imgName)
    }
}