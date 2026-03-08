package com.clip.api.card.controller.dto

data class ArticleCardResponse(
    val cardId: Long,
    val profileImgUrl: String,
    val nickname: String,
    val cardContent: String,
    val isRead: Boolean,
    val writerProfileImgUrls: List<String>,
    val totalWriterCnt: Int,
)
