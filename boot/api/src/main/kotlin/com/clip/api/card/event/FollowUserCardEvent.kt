package com.clip.api.card.event

data class FollowUserCardEvent(
    val cardId: Long,
    val cardCreatorId: Long,
    val nickname: String,
    val userImgName: String? = null,
    val content: String,
)
