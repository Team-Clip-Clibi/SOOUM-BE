package com.clip.api.card.mapper

import com.clip.api.card.controller.dto.*
import com.clip.data.card.entity.*
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.card.entity.parenttype.CardType
import com.clip.data.common.deactivatewords.DeactivateTagWords
import com.clip.data.member.entity.Member
import com.clip.data.tag.entity.Tag
import com.clip.global.exception.IllegalArgumentException.ParameterNotFoundException
import com.clip.global.util.CardUtil
import com.clip.infra.s3.S3ImgPathProperties
import com.clip.infra.s3.S3ImgService
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class CardMapper(
    private val geometryFactory: GeometryFactory = GeometryFactory(),
    private val s3ImgService: S3ImgService,
    private val s3ImgPathProperties: S3ImgPathProperties
) {
    fun toFeedCard(
        request: CreateFeedCardRequest,
        requestIp: String,
        member: Member,
    ): FeedCard =
        FeedCard(
            request.content,
            request.font,
            if (request.isDistanceShared) {
                geometryFactory.createPoint(
                    Coordinate(
                        request.longitude ?: throw ParameterNotFoundException("longitude"),
                        request.latitude ?: throw ParameterNotFoundException("longitude")
                    )
                )
            } else null,
            request.imgType,
            request.imgName,
            member,
            request.isStory,
            request.tags.isEmpty() || DeactivateTagWords.deactivateWordsList.none { request.tags.contains(it) },
            requestIp,
        )

    fun toCommentCard(
        request: CreateCommentCardRequest,
        member: Member,
        parentCardType: CardType,
        parentCardId: Long,
        masterCardId: Long,
        ip: String
    ): CommentCard =
        CommentCard(
            request.content,
            request.font,
            if (request.isDistanceShared) {
                geometryFactory.createPoint(
                    Coordinate(
                        request.longitude ?: throw ParameterNotFoundException("longitude"),
                        request.latitude ?: throw ParameterNotFoundException("longitude")
                    )
                )
            } else null,
            request.imgType,
            request.imgName,
            member,
            parentCardType,
            parentCardId,
            masterCardId,
            ip
        )

    fun toFeedCardDetailResponse(
        card: FeedCard,
        writer: Member,
        feedLikes: List<FeedLike>,
        comments: List<CommentCard>,
        distance: String?,
        userId: Long,
        tags: List<Tag>,
        feedViews: Long,
        isReported: Boolean
    ): FeedCardDetailResponse =
        FeedCardDetailResponse(
            cardId = card.pk,
            likeCnt = CardUtil.countLikes(card, feedLikes),
            commentCardCnt = CardUtil.countComments(card, comments),
            cardImgUrl = when(card.imgType){
                CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(card.imgName)
                CardImgType.USER -> s3ImgService.generateUserCardImgUrl(card.imgName)
            },
            cardImgName = card.imgName,
            cardContent = card.content,
            font = card.font,
            distance = distance,
            createdAt = card.createdAt,
            isAdminCard = writer.role == com.clip.data.member.entity.Role.ADMIN,
            storyExpirationTime = if (card.isStory) card.createdAt.plusHours(24) else null,
            memberId = writer.pk,
            nickname = writer.nickname,
            profileImgUrl = writer.profileImgName?.let { s3ImgService.generateGetPresignedUrl(s3ImgPathProperties.profileImg, it) },
            isLike = CardUtil.isLiked(card, feedLikes, userId),
            isCommentWritten = CardUtil.isWrittenCommentCard(card, comments, userId),
            tags = tags.map { TagResponse(it.pk, it.content) },
            isOwnCard = writer.pk == userId,
            isFeedCard = true,
            visitedCnt = feedViews,
            isReported = isReported
        )

    fun toCommentCardDetailResponse(
        card: CommentCard,
        writer: Member,
        feedLikes: List<CommentLike>,
        comments: List<CommentCard>,
        distance: String?,
        userId: Long,
        tags: List<Tag>,
        parentCard: Card?,
        commentView: Long,
        isReported: Boolean
    ) : CommentCardDetailResponse =
        CommentCardDetailResponse (
            cardId = card.pk,
            likeCnt = CardUtil.countLikes(card, feedLikes),
            commentCardCnt = CardUtil.countComments(card, comments),
            cardImgUrl = when(card.imgType){
                CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(card.imgName)
                CardImgType.USER -> s3ImgService.generateUserCardImgUrl(card.imgName)
            },
            cardImgName = card.imgName,
            cardContent = card.content,
            font = card.font,
            distance = distance,
            createdAt = card.createdAt,
            isAdminCard = writer.role == com.clip.data.member.entity.Role.ADMIN,
            memberId = writer.pk,
            nickname = writer.nickname,
            profileImgUrl = writer.profileImgName?.let { s3ImgService.generateGetPresignedUrl(s3ImgPathProperties.profileImg, it) },
            isLike = CardUtil.isLiked(card, feedLikes, userId),
            isCommentWritten = CardUtil.isWrittenCommentCard(card, comments, userId),
            tags = tags.map { TagResponse(it.pk, it.content) },
            isOwnCard = writer.pk == userId,
            previousCardId = parentCard?.pk?.toString() ?: "",
            isPreviousCardDeleted = parentCard == null,
            previousCardImgUrl = parentCard?.let {
                when (it.imgType){
                    CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(it.imgName)
                    CardImgType.USER -> s3ImgService.generateUserCardImgUrl(it.imgName)
                }
            },
            visitedCnt = commentView,
            isReported = isReported
        )

    fun toCommentResponse(
        card: CommentCard,
        childComments: List<CommentCard>,
        commentLikes: List<CommentLike>,
        distance: String?
    ): CommentCardResponse =
        CommentCardResponse(
            cardId = card.pk,
            likeCnt = CardUtil.countLikes(card, commentLikes),
            commentCardCnt = CardUtil.countComments(card, childComments),
            cardImgUrl = when(card.imgType){
                CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(card.imgName)
                CardImgType.USER -> s3ImgService.generateUserCardImgUrl(card.imgName)
            },
            cardImgName = card.imgName,
            cardContent = card.content,
            font = card.font,
            distance = distance,
            createdAt = card.createdAt,
            isAdminCard = card.writer.role == com.clip.data.member.entity.Role.ADMIN
    )

}