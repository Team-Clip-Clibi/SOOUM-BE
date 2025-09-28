package com.clip.api.card.mapper

import com.clip.api.card.controller.dto.FeedResponse
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.FeedLike
import com.clip.data.card.entity.imgtype.CardImgType
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
    ): FeedResponse =
        FeedResponse(
            cardId = targetFeedCard.pk.toString(),
            cardImgUrl = when(targetFeedCard.imgType){
                CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(targetFeedCard.imgName)
                CardImgType.USER -> s3ImgService.generateUserCardImgUrl(targetFeedCard.imgName)
            },
            cardImgName = targetFeedCard.imgName,
            likeCnt = CardUtil.countLikes(targetFeedCard, feedLikes),
            commentCardCnt = CardUtil.countComments(targetFeedCard, comments),
            font = targetFeedCard.font,
            distance = distance,
            createdAt = targetFeedCard.createdAt,
            storyExpirationTime = if (targetFeedCard.isStory) targetFeedCard.createdAt.plusHours(24) else null,
            isAdminCard = targetFeedCard.writer.role.equals(Role.ADMIN)
        )

}