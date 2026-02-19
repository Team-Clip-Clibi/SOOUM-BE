package com.clip.api.abtest.service

import com.clip.api.abtest.controller.dto.ArticleCardAbTestResponse
import com.clip.api.abtest.mapper.ArticleCardAbTestMapper
import com.clip.data.abtest.entity.AbGroup
import com.clip.data.abtest.service.TempAbHomeAdminCardUserGroupService
import com.clip.data.abtest.service.TempAbHomeAdminCardUserRetrieveDetailService
import com.clip.data.card.service.ArticleCardService
import com.clip.data.card.service.CommentCardService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AbUseCase(
    private val articleCardService: ArticleCardService,
    private val commentCardService: CommentCardService,
    private val articleCardAbTestMapper: ArticleCardAbTestMapper,
    private val tempAbHomeAdminCardUserGroupService: TempAbHomeAdminCardUserGroupService,
    private val tempAbHomeAdminCardUserRetrieveDetailService: TempAbHomeAdminCardUserRetrieveDetailService,
) {

    @Transactional(readOnly = true)
    fun getLatestArticleCard(
        userId: Long
    ): ArticleCardAbTestResponse? {
        val abGroup = tempAbHomeAdminCardUserGroupService.findUserGroup(userId)
        if (abGroup == AbGroup.NONE) return null

        val feedCard = articleCardService.findLatestArticleFeedCard().orElse(null) ?: return null
        val isRead = tempAbHomeAdminCardUserRetrieveDetailService.isReadByUser(userId, feedCard.pk)

        return when (abGroup) {
            AbGroup.A -> articleCardAbTestMapper.toTypeAResponse(
                feedCard = feedCard,
                isRead = isRead
            )
            AbGroup.B -> {
                val commentWriters = commentCardService.findChildCommentCardListWithWriter(feedCard.pk)
                    .map { it.writer }
                    .distinctBy { it.pk }

                articleCardAbTestMapper.toTypeBResponse(
                    feedCard = feedCard,
                    commentWriters = commentWriters,
                    isRead = isRead
                )
            }
            AbGroup.NONE -> null
        }
    }
}

