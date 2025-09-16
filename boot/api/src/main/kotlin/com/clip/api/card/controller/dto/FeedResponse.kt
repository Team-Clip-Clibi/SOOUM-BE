package com.clip.api.card.controller.dto

import com.clip.data.card.entity.font.Font
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class FeedResponse(
    val cardId: String,
    val likeCnt: Int,
    val commentCardCnt: Int,
    val cardImgUrl: String,
    val font: Font,
    val distance: String?,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val createdAt: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val storyExpirationTime: LocalDateTime?,
)
