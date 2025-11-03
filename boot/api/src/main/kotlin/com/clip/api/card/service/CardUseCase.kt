package com.clip.api.card.service

import com.clip.api.card.controller.dto.CardDetailResponse
import com.clip.api.card.controller.dto.CommentCardResponse
import com.clip.api.card.controller.dto.CreateCommentCardRequest
import com.clip.api.card.controller.dto.CreateFeedCardRequest
import com.clip.api.card.mapper.CardMapper
import com.clip.api.card.util.DistanceDisplayUtil
import com.clip.api.notification.event.CardFCMEvent
import com.clip.api.notification.service.NotificationUseCase
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.entity.*
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
import com.clip.global.exception.IllegalArgumentException
import com.clip.global.exception.IllegalStateException
import com.clip.global.exception.ImageException
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3ImgService
import jakarta.persistence.EntityManager
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
    private val commentViewService: CommentViewService,
    private val feedViewService: FeedViewService,
    private val entityManager: EntityManager
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
        val member = handleBanStatus(memberService.findMember(userId))

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

        val savedTags = tagService.saveAllAndIncrementTagCnt(createFeedCardRequest.tags.distinct().toMutableList())
        val restoredTags = createFeedCardRequest.tags.mapNotNull { tagName ->
            savedTags.find { it.content == tagName }
        }
        feedTagService.saveAll(feedCard, restoredTags)
    }

    fun createCommentCard(
        httpServletRequest: HttpServletRequest,
        createCommentCardRequest: CreateCommentCardRequest,
        cardId: Long,
        userId: Long
    ) {
        val member = handleBanStatus(memberService.findMember(userId))

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

        if (isUserImgType(createCommentCardRequest.imgType))
            cardImgService.updateCardImg(commentCard, createCommentCardRequest.imgName)

        val savedTags = tagService.saveAllAndNoIncrementTagCnt(createCommentCardRequest.tags.distinct().toMutableList())
        val restoredTags = createCommentCardRequest.tags.mapNotNull { tagName ->
            savedTags.find { it.content == tagName }
        }
        commentTagService.saveAll(commentCard, restoredTags)

        if (parentCard.writer.pk != userId) {
            val commentWriteNotification =
                notificationUseCase.saveCommentCardHistory(userId, commentCard.pk, parentCard)
            if (parentCard.writer.isAllowNotify)
                applicationEventPublisher.publishEvent(
                    CardFCMEvent(
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

    @Transactional
    fun getFeedCardDetail(latitude: Double?, longitude: Double?, cardId: Long, userId: Long): CardDetailResponse {
        val card: Card = getCard(cardId)
        val writer = card.writer
        val tags = tagService.getTagsByCard(card)
        val distance = DistanceDisplayUtil.calculateAndFormat(card.location, latitude, longitude)
        val requester = memberService.findMember(userId)

        return when (card) {
            is FeedCard -> {
                if (card.writer.pk != userId)
                    feedViewService.save(
                        FeedView(
                            card,
                            requester
                        )
                    )

                val feedLikes = feedLikeService.findByTargetCard(cardId)
                val comments = commentCardService.findChildCommentCardList(cardId)
                val feedViews = feedViewService.countView(card)
                cardMapper.toFeedCardDetailResponse(
                    card,
                    writer,
                    feedLikes,
                    comments,
                    distance,
                    userId,
                    tags,
                    feedViews
                )
            }

            is CommentCard -> {
                if (card.writer.pk != userId)
                    commentViewService.save(
                        CommentView(
                            card,
                            requester
                        )
                    )

                val parentCard = when (card.parentCardType) {
                    CardType.FEED_CARD -> feedCardService.findFeedCardOrNull(card.parentCardPk)
                    CardType.COMMENT_CARD -> commentCardService.findCommentCardOrNull(card.parentCardPk)
                    else -> null
                }
                val commentLikes = commentLikeService.findByTargetCard(cardId)
                val comments = commentCardService.findChildCommentCardList(cardId)
                val commentViews = commentViewService.countView(card)
                cardMapper.toCommentCardDetailResponse(
                    card,
                    writer,
                    commentLikes,
                    comments,
                    distance,
                    userId,
                    tags,
                    parentCard,
                    commentViews
                )
            }

            else -> throw RuntimeException()

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
            throw IllegalArgumentException.NotResourceOwnerException("본인이 작성한 카드만 삭제할 수 있습니다.")

        when (card) {
            is FeedCard -> {
                deleteFeedCardDependencies(card)
                entityManager.clear()
                feedCardService.deleteFeedCard(card.pk)
            }

            is CommentCard -> {
                deleteCommentCardDependencies(card)
                entityManager.clear()
                commentCardService.deleteCommentCard(card.pk)
            }

            else -> throw RuntimeException()
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
            else -> throw RuntimeException()
        }
    }

    private fun getCardType(card: Card): CardType =
        if (card is FeedCard) CardType.FEED_CARD else CardType.COMMENT_CARD


    private fun getCard(cardId: Long): Card {
        return if (feedCardService.isExistFeedCard(cardId))
            feedCardService.findFeedCard(cardId)
        else if (commentCardService.isExistCommentCard(cardId))
            commentCardService.findCommentCard(cardId)
        else throw IllegalArgumentException.CardNotFoundException()
    }

    private fun handleBanStatus(member: Member): Member {
        if (member.role == Role.BANNED) {
            if (isBanPeriodExpired(member)){
                member.unban()
            }else {
                throw IllegalStateException.CardWriteNotAllowedException()
            }
        }
        return member
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

}