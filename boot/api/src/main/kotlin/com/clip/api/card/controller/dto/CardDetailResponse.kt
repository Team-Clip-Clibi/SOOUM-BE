package com.clip.api.card.controller.dto

import com.clip.data.card.entity.font.Font
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

sealed class CardDetailResponse(
    val cardId: Long,
    val likeCnt: Int,
    val commentCardCnt: Int,
    val cardImgUrl: String,
    val cardImgName: String,
    val cardContent: String,
    val font: Font,
    val distance: String?,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val createdAt: LocalDateTime,
    val isAdminCard: Boolean,
    val isReported: Boolean
)

class FeedCardDetailResponse(
    cardId: Long,
    likeCnt: Int,
    commentCardCnt: Int,
    cardImgUrl: String,
    cardImgName: String,
    cardContent: String,
    font: Font,
    distance: String?,
    createdAt: LocalDateTime,
    isAdminCard: Boolean,
    isReported: Boolean,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val storyExpirationTime: LocalDateTime?,
    val memberId: Long,
    val nickname: String,
    val profileImgUrl: String?,
    val isLike: Boolean,
    val isCommentWritten: Boolean,
    val tags: List<TagResponse>,
    val isOwnCard: Boolean,
    val isFeedCard: Boolean,
    val visitedCnt: Long,
) : CardDetailResponse(
    cardId = cardId,
    likeCnt = likeCnt,
    commentCardCnt = commentCardCnt,
    cardImgUrl = cardImgUrl,
    cardImgName = cardImgName,
    cardContent = cardContent,
    font = font,
    distance = distance,
    createdAt = createdAt,
    isAdminCard = isAdminCard,
    isReported = isReported,
)

class CommentCardDetailResponse(
    cardId: Long,
    likeCnt: Int,
    commentCardCnt: Int,
    cardImgUrl: String,
    cardImgName: String,
    cardContent: String,
    font: Font,
    distance: String?,
    createdAt: LocalDateTime,
    isAdminCard: Boolean,
    isReported: Boolean,
    val memberId: Long,
    val nickname: String,
    val profileImgUrl: String?,
    val isLike: Boolean,
    val isCommentWritten: Boolean,
    val tags: List<TagResponse>,
    val isOwnCard: Boolean,
    val previousCardId: String,
    val isPreviousCardDeleted: Boolean,
    val previousCardImgUrl: String?,
    val visitedCnt: Long,
) : CardDetailResponse(
    cardId = cardId,
    likeCnt = likeCnt,
    commentCardCnt = commentCardCnt,
    cardImgUrl = cardImgUrl,
    cardImgName = cardImgName,
    cardContent = cardContent,
    font = font,
    distance = distance,
    createdAt = createdAt,
    isAdminCard = isAdminCard,
    isReported = isReported,
)

data class TagResponse (
    val tagId: Long,
    val name: String,
)