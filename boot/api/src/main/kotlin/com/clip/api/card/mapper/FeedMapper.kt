package com.clip.api.card.mapper

import com.clip.api.card.controller.dto.FeedCardResponse
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.FeedLike
import com.clip.data.card.entity.font.Font
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.card.repository.projection.DistanceFeedCardDto
import com.clip.data.member.entity.Role
import com.clip.global.util.CardUtil
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Component

@Component
class FeedMapper(
    private val s3ImgService: S3ImgService,
) {
    fun toFeedResponse(
        targetFeedCard: FeedCard,
        comments: List<CommentCard>,
        feedLikes: List<FeedLike>,
        distance: String?,
    ): FeedCardResponse =
        FeedCardResponse(
            cardId = targetFeedCard.pk,
            cardImgUrl = when(targetFeedCard.imgType){
                CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(targetFeedCard.imgName)
                CardImgType.USER -> s3ImgService.generateUserCardImgUrl(targetFeedCard.imgName)
            },
            cardImgName = targetFeedCard.imgName,
            cardContent = targetFeedCard.content,
            likeCnt = CardUtil.countLikes(targetFeedCard, feedLikes),
            commentCardCnt = CardUtil.countComments(targetFeedCard, comments),
            font = targetFeedCard.font,
            distance = distance,
            createdAt = targetFeedCard.createdAt,
            storyExpirationTime = if (targetFeedCard.isStory) targetFeedCard.createdAt.plusHours(24) else null,
            isAdminCard = targetFeedCard.writer.role == Role.ADMIN
        )

    fun toFeedResponse(
        feedcard: DistanceFeedCardDto,
        comments: List<CommentCard>,
        feedLikes: List<FeedLike>,
        distance: String?,
    ): FeedCardResponse =
        FeedCardResponse(
            cardId = feedcard.pk,
            cardImgUrl = when(CardImgType.valueOf(feedcard.imgType)){
                CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(feedcard.imgName)
                CardImgType.USER -> s3ImgService.generateUserCardImgUrl(feedcard.imgName)
            },
            cardImgName = feedcard.imgName,
            cardContent = feedcard.content,
            likeCnt = feedLikes.count { it.targetCard?.pk == feedcard.pk },
            commentCardCnt = comments.count { it.parentCardPk == feedcard.pk },
            font = Font.valueOf(feedcard.font),
            distance = distance,
            createdAt = feedcard.createdAt,
            storyExpirationTime = if (feedcard.isStory) feedcard.createdAt.plusHours(24) else null,
            isAdminCard = Role.valueOf(feedcard.role) == Role.ADMIN
        )


}