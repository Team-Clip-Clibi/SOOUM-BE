package com.clip.api.card.controller.dto

data class CardContentsResponse(
    val cardContents: List<CardContent>
)

data class CardContent(
    val cardId: Long,
    val cardImgName: String,
    val cardImgUrl: String,
    val cardContent: String,
)
