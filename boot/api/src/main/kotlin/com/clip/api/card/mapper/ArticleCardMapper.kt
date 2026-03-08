package com.clip.api.card.mapper

import com.clip.api.card.controller.dto.ArticleCardResponse
import com.clip.data.card.entity.FeedCard
import com.clip.data.member.entity.Member
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Component

@Component
class ArticleCardMapper(
    private val s3ImgService: S3ImgService,
) {

    fun toResponse(
        feedCard: FeedCard,
        commentWriters: List<Member>,
        isRead: Boolean
    ): ArticleCardResponse {
        val writer = feedCard.writer
        val writerProfileImgUrls = commentWriters
            .take(3)
            .map { generateProfileImgUrl(it.profileImgName) }

        return ArticleCardResponse(
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
