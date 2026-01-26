package com.clip.api.abtest.controller.dto

enum class ArticleCardAbTestType {
    A, B, NONE
}

sealed class ArticleCardAbTestResponse(
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
) : ArticleCardAbTestResponse(
    abTestType = ArticleCardAbTestType.A,
    profileImgUrl = profileImgUrl,
    nickname = nickname,
    cardContent = cardContent,
    isRead = isRead,
)

class ArticleCardTypeBResponse(
    profileImgUrl: String,
    nickname: String,
    cardContent: String,
    isRead: Boolean,
    val writerProfileImgUrls: List<String>,
    val totalWriterCnt: Int,
) : ArticleCardAbTestResponse(
    abTestType = ArticleCardAbTestType.B,
    profileImgUrl = profileImgUrl,
    nickname = nickname,
    cardContent = cardContent,
    isRead = isRead,
)
