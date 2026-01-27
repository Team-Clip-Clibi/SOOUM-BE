package com.clip.api.card.service.aspect

import com.clip.api.card.controller.dto.FeedCardResponse
import com.clip.data.abtest.service.TempAbHomeAdminCardUserGroupService
import com.clip.data.abtest.service.TempAbHomeAdminCardUserRetrieveDetailService
import com.clip.data.card.service.ArticleCardService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

//AB_TEST HomeAdminCardUserTest
@Component
@Aspect
class FeedAspect(
    private val articleCardService: ArticleCardService,
    private val tempAbHomeAdminCardUserRetrieveDetailService: TempAbHomeAdminCardUserRetrieveDetailService,
    private val tempAbHomeAdminCardUserGroupService: TempAbHomeAdminCardUserGroupService,
) {

    @Around("execution(* com.clip.api.abtest.controller.AbController.getLatestArticleCard(..))")
    fun displayLogAround(pjp: ProceedingJoinPoint): Any {
        val userId = pjp.args[0] as Long
        val latestArticleCard = articleCardService.findLatestArticleFeedCard()
        val userGroup =
            tempAbHomeAdminCardUserGroupService.findTempAbHomeAdminCardUserGroup(userId)

        if (latestArticleCard.isEmpty || userGroup.isEmpty) {
            return pjp.proceed()
        }
        tempAbHomeAdminCardUserRetrieveDetailService.saveDisplayTypeLog(userGroup.get(), latestArticleCard.get())
        return pjp.proceed()
    }

    @Around("execution(* com.clip.api.card.service.DistanceFeedUseCase.findDistanceFeeds(..))")
    fun filterAdminArticlesFromDistanceFeeds(pjp: ProceedingJoinPoint): Any {
        return filterAdminArticles(pjp)
    }

    @Around("execution(* com.clip.api.card.service.LatestFeedUseCase.findLatestFeeds(..))")
    fun filterAdminArticlesFromLatestFeeds(pjp: ProceedingJoinPoint): Any {
        return filterAdminArticles(pjp)
    }

    @Around("execution(* com.clip.api.card.service.PopularFeedUseCase.findPopularFeeds(..))")
    fun filterAdminArticlesFromPopularFeeds(pjp: ProceedingJoinPoint): Any {
        return filterAdminArticles(pjp)
    }

    private fun filterAdminArticles(pjp: ProceedingJoinPoint): Any {
        val result = pjp.proceed()

        if (result !is List<*>) {
            return result
        }

        @Suppress("UNCHECKED_CAST")
        val feedResponses = result as List<FeedCardResponse>

        return feedResponses.filter { feedResponse ->
            val isAdmin = feedResponse.isAdminCard
            val isArticle = articleCardService.isArticleCard(feedResponse.cardId)

            // ADMIN이 작성한 아티클이면 제외, 그 외에는 포함
            !(isAdmin && isArticle)
        }
    }
}

