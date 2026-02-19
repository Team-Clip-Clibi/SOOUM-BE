package com.clip.api.abtest.mapper

import com.clip.api.abtest.controller.dto.ArticleCardAbTestResponse
import com.clip.api.abtest.controller.dto.ArticleCardTypeAResponse
import com.clip.api.abtest.controller.dto.ArticleCardTypeBResponse
import com.clip.data.card.entity.FeedCard
import com.clip.data.member.entity.Member
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Component

@Component
class ArticleCardAbTestMapper(
    private val s3ImgService: S3ImgService,
) {

    fun toTypeAResponse(
        feedCard: FeedCard,
        isRead: Boolean
    ): ArticleCardAbTestResponse {
        val writer = feedCard.writer
        return ArticleCardTypeAResponse(
            profileImgUrl = generateProfileImgUrl(writer.profileImgName),
            nickname = writer.nickname,
            cardContent = feedCard.content,
            isRead = isRead,
            cardId = feedCard.pk
        )
    }

    fun toTypeBResponse(
        feedCard: FeedCard,
        commentWriters: List<Member>,
        isRead: Boolean
    ): ArticleCardAbTestResponse {
        val writer = feedCard.writer
        val writerProfileImgUrls = commentWriters
            .take(3)
            .map { generateProfileImgUrl(it.profileImgName) }

        return ArticleCardTypeBResponse(
            profileImgUrl = generateProfileImgUrl(writer.profileImgName),
            nickname = writer.nickname,
            cardContent = feedCard.content,
            isRead = isRead,
            writerProfileImgUrls = writerProfileImgUrls,
            totalWriterCnt = commentWriters.size,
            cardId = feedCard.pk
        )
    }

    private fun generateProfileImgUrl(profileImgName: String?): String {
        return profileImgName?.let { s3ImgService.generateProfileImgUrl(it) } ?: ""
    }
}

