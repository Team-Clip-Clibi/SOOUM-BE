package com.clip.api.card.service

import com.clip.api.card.controller.dto.ArticleCardResponse
import com.clip.api.card.mapper.ArticleCardMapper
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.service.ArticleCardService
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedViewService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleCardUseCase(
    private val articleCardService: ArticleCardService,
    private val commentCardService: CommentCardService,
    private val feedViewService: FeedViewService,
    private val articleCardMapper: ArticleCardMapper,
) {

    @Transactional(readOnly = true)
    fun getLatestArticleCard(
        userId: Long
    ): ArticleCardResponse? {
        val feedCard = articleCardService.findLatestArticleFeedCard().orElse(null) ?: return null
        return toArticleCardResponse(feedCard, userId)
    }

    @Transactional(readOnly = true)
    fun getLatestArticleCards(
        userId: Long
    ): List<ArticleCardResponse> =
        articleCardService.findLatestArticleFeedCards()
            .map { toArticleCardResponse(it, userId) }

    private fun toArticleCardResponse(
        feedCard: FeedCard,
        userId: Long
    ): ArticleCardResponse {
        val isRead = feedViewService.isViewedByMember(feedCard.pk, userId)
        val commentWriters = commentCardService.findChildCommentCardListWithWriter(feedCard.pk)
            .map { it.writer }
            .distinctBy { it.pk }

        return articleCardMapper.toResponse(
            feedCard = feedCard,
            commentWriters = commentWriters,
            isRead = isRead
        )
    }
}
