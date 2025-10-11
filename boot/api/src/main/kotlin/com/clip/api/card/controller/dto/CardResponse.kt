package com.clip.api.card.controller.dto

import com.clip.data.card.entity.font.Font
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

sealed class CardResponse(
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
)

class CommentCardResponse(
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
) : CardResponse(
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
)

class FeedCardResponse(
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val storyExpirationTime: LocalDateTime?,
) : CardResponse(
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
)