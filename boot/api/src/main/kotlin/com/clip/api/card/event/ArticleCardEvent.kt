package com.clip.api.card.event

data class ArticleCardEvent(
    val cardId: Long,
    val content: String,
    val imgName: String?,
)
