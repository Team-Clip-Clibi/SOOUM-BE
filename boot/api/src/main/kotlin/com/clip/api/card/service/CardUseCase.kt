package com.clip.api.card.service

import com.clip.api.card.controller.dto.CardContent
import com.clip.api.card.controller.dto.CardContentsResponse
import com.clip.api.card.controller.dto.CardDetailResponse
import com.clip.api.card.controller.dto.CommentCardResponse
import com.clip.api.card.controller.dto.CreateCommentCardRequest
import com.clip.api.card.controller.dto.CreateFeedCardRequest
import com.clip.api.card.mapper.CardMapper
import com.clip.api.card.util.DistanceDisplayUtil
import com.clip.api.notification.event.NotificationFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.entity.Card
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.card.entity.parenttype.CardType
import com.clip.data.card.service.*
import com.clip.data.img.service.CardImgService
import com.clip.data.member.entity.Member
import com.clip.data.member.entity.Role
import com.clip.data.member.service.MemberService
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.data.notification.service.NotificationHistoryService
import com.clip.data.report.service.CommentReportService
import com.clip.data.report.service.FeedReportService
import com.clip.data.tag.service.CommentTagService
import com.clip.data.tag.service.FeedTagService
import com.clip.data.tag.service.TagService
import com.clip.global.exception.IllegalArgumentException.ParameterNotFoundException
import com.clip.global.exception.ImageException
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3ImgService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

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
    private val feedLikeService: FeedLikeService,
    private val commentLikeService: CommentLikeService,
    private val blockMemberService: BlockMemberService,
    private val feedReportService: FeedReportService,
    private val commentReportService: CommentReportService,
    private val popularFeedService: PopularFeedService,
    private val notificationHistoryService: NotificationHistoryService,

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
        if (isUserImgType(createFeedCardRequest.imgType))
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

        val parentCard = getCard(cardId)
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

        if (parentCard.writer.pk != userId) {
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

    fun getFeedCardDetail(latitude: Double?, longitude: Double?, cardId: Long, userId: Long): CardDetailResponse {
        val card: Card = getCard(cardId)
        val writer = card.writer
        val tags = tagService.getTagsByCard(card)
        val distance = DistanceDisplayUtil.calculateAndFormat(card.location, latitude, longitude)

        return when (card) {
            is FeedCard -> {
                feedCardService.increaseViewCnt(card.pk, userId)
                val feedLikes = feedLikeService.findByTargetCardIds(listOf(cardId))
                val comments = commentCardService.findCommentCardsIn(listOf(cardId))
                cardMapper.toFeedCardDetailResponse(card, writer, feedLikes, comments, distance, userId, tags)
            }
            is CommentCard -> {
                commentCardService.increaseViewCnt(card.pk, userId)
                val parentCard = when (card.parentCardType) {
                    CardType.FEED_CARD -> feedCardService.findFeedCardOrNull(card.parentCardPk)
                    CardType.COMMENT_CARD -> commentCardService.findCommentCardOrNull(card.parentCardPk)
                    else -> null
                }
                val commentLikes = commentLikeService.findByTargetCardIds(listOf(cardId))
                val comments = commentCardService.findCommentCardsIn(listOf(cardId))
                cardMapper.toCommentCardDetailResponse(card, writer, commentLikes, comments, distance, userId, tags, parentCard)
            }
            else -> throw IllegalArgumentException()

        }
    }

    fun getCommentCard(
        latitude: Double?,
        longitude: Double?,
        lastId: Long?,
        cardId: Long,
        userId: Long
    ): List<CommentCardResponse> {

        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val comments = commentCardService.findCommentsByLastPk(cardId, Optional.ofNullable(lastId), blockedMembers)
        val commentLikes = commentLikeService.findByTargetCardIds(comments.map { it.pk })
        val childComments = commentCardService.findChildCommentsByParents(comments.map { it.pk })

        return comments.map {
            cardMapper.toCommentResponse(
                it,
                childComments,
                commentLikes,
                DistanceDisplayUtil.calculateAndFormat(it.location, latitude, longitude)
            )
        }
    }

    @Transactional
    fun deleteFeedCardWithRelations(feedCard: FeedCard) {
        feedTagService.deleteByFeedCardPk(feedCard.pk)
        feedLikeService.deleteAllFeedLikes(feedCard.pk)
        feedCardService.deleteFeedCard(feedCard.pk)
    }

    @Transactional
    fun deleteCommentCardWithRelations(commentCard: CommentCard) {
        commentTagService.deleteByCommentCardPk(commentCard.pk)
        commentLikeService.deleteAllFeedLikes(commentCard.pk)
        commentCardService.deleteCommentCard(commentCard.pk)
    }

    @Transactional
    fun deleteCard(cardId: Long, userId: Long) {
        val card = getCard(cardId)
        if (card.writer.pk != userId)
            throw IllegalArgumentException("본인이 작성한 카드만 삭제할 수 있습니다.")

        when (card) {
            is FeedCard -> {
                deleteFeedCardDependencies(card)
                feedCardService.deleteFeedCard(card.pk)
            }
            is CommentCard -> {
                deleteCommentCardDependencies(card)
                commentCardService.deleteCommentCard(card.pk)
            }
            else -> throw IllegalArgumentException()
        }
    }

    private fun deleteFeedCardDependencies(feedCard: FeedCard) {
        feedTagService.deleteByFeedCardPk(feedCard.pk)
        cardImgService.deleteUserUploadPic(feedCard.imgName)
        feedLikeService.deleteAllFeedLikes(feedCard.pk)
        feedReportService.deleteReport(feedCard.pk)
        popularFeedService.deletePopularCard(feedCard.pk)
        notificationHistoryService.deleteNotification(feedCard.pk)
    }

    private fun deleteCommentCardDependencies(commentCard: CommentCard) {
        commentTagService.deleteByCommentCardPk(commentCard.pk)
        cardImgService.deleteUserUploadPic(commentCard.imgName)
        commentLikeService.deleteAllFeedLikes(commentCard.pk)
        commentReportService.deleteReport(commentCard)
        notificationHistoryService.deleteNotification(commentCard.pk)
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


    private fun getCard(cardId: Long): Card {
        if (feedCardService.isExistFeedCard(cardId))
            return feedCardService.findFeedCard(cardId)
        else if (commentCardService.isExistCommentCard(cardId))
            return commentCardService.findCommentCard(cardId)
        else throw ParameterNotFoundException("카드(id: $cardId)를 찾을 수 없습니다.", cardId)
    }

    private fun isBanPeriodExpired(member: Member): Boolean =
        (member.untilBan?.isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) == true)
                && member.role == Role.BANNED

    private fun isUserImgType(cardImgType: CardImgType) =
        cardImgType == CardImgType.USER

    private fun validUserCardImg(imgName: String) {
        if (!s3ImgService.isCardImgSaved(imgName))
            throw ImageException.ImageNotFoundException(imgName = imgName)

        if (rekognitionService.isModeratingCardImg(imgName))
            throw ImageException.InvalidImageException(imgName = imgName)
    }

    @Transactional(readOnly = true)
    fun getUserFeedCards(lastId: Long?, userId: Long): CardContentsResponse =
        feedCardService.findFeedCardsByUser(userId, lastId)
            .map { card ->
                CardContent(
                    card.pk,
                    card.imgName,
                    if (card.imgType.equals(CardImgType.USER)) {
                        s3ImgService.generateUserCardImgUrl(card.imgName)
                    } else {
                        s3ImgService.generateDefaultCardImgUrl(card.imgName)
                    },
                    card.content
                )
            }.toList().let { CardContentsResponse(it) }


    @Transactional(readOnly = true)
    fun getMyCommentCards(lastId: Long?, userId: Long): CardContentsResponse =
        commentCardService.findCommentCardsByUser(userId, lastId)
            .map { card ->
                CardContent(
                    card.pk,
                    card.imgName,
                    if (card.imgType.equals(CardImgType.USER)) {
                        s3ImgService.generateUserCardImgUrl(card.imgName)
                    } else {
                        s3ImgService.generateDefaultCardImgUrl(card.imgName)
                    },
                    card.content
                )
            }.toList().let { CardContentsResponse(it) }

}