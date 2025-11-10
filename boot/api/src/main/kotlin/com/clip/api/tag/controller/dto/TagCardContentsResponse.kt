package com.clip.api.tag.controller.dto

import com.clip.data.card.entity.font.Font

data class TagCardContentsResponse(
    val cardContents: List<CardContent>,
    val isFavorite: Boolean
)

data class CardContent(
    val cardId: Long,
    val cardImgName: String,
    val cardImgUrl: String,
    val cardContent: String,
    val font: Font
)
