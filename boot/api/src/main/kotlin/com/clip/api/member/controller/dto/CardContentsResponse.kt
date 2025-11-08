package com.clip.api.member.controller.dto

import com.clip.data.card.entity.font.Font

data class CardContentsResponse(
    val cardContents: List<CardContent>
)

data class CardContent(
    val cardId: Long,
    val cardImgName: String,
    val cardImgUrl: String,
    val cardContent: String,
    val font: Font
)
