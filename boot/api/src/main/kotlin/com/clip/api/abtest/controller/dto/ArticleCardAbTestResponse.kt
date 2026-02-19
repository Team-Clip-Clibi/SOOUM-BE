package com.clip.api.abtest.controller.dto

enum class ArticleCardAbTestType {
    A, B, NONE
}

sealed class ArticleCardAbTestResponse(
    val cardId: Long,
    val abTestType: ArticleCardAbTestType,
    val profileImgUrl: String,
    val nickname: String,
    val cardContent: String,
    val isRead: Boolean,
)

class ArticleCardTypeAResponse(
    profileImgUrl: String,
    nickname: String,
    cardContent: String,
    isRead: Boolean,
    cardId: Long,
) : ArticleCardAbTestResponse(
    abTestType = ArticleCardAbTestType.A,
    profileImgUrl = profileImgUrl,
    nickname = nickname,
    cardContent = cardContent,
    isRead = isRead,
    cardId = cardId
)

class ArticleCardTypeBResponse(
    profileImgUrl: String,
    nickname: String,
    cardContent: String,
    isRead: Boolean,
    val writerProfileImgUrls: List<String>,
    val totalWriterCnt: Int,
    cardId: Long,
) : ArticleCardAbTestResponse(
    abTestType = ArticleCardAbTestType.B,
    profileImgUrl = profileImgUrl,
    nickname = nickname,
    cardContent = cardContent,
    isRead = isRead,
    cardId = cardId,
)
