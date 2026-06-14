package com.clip.api.card.service

import com.clip.api.card.controller.dto.FeedCardResponse
import com.clip.api.card.mapper.FeedMapper
import com.clip.api.card.util.DistanceDisplayUtil
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.service.ArticleCardService
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedLikeService
import com.clip.data.card.service.PopularFeedService
import com.clip.data.poll.service.PollVoteService
import org.springframework.stereotype.Service

@Service
class PopularFeedUseCase(
    private val popularFeedService: PopularFeedService,
    private val blockMemberService: BlockMemberService,
    private val feedLikeService: FeedLikeService,
    private val commentCardService: CommentCardService,
    private val articleCardService: ArticleCardService,
    private val feedMapper: FeedMapper,
    private val pollVoteService: PollVoteService,
) {

    fun findPopularFeeds(latitude: Double?, longitude: Double?, userId: Long): List<FeedCardResponse> {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val popularFeeds = popularFeedService.getPopularFeeds(blockedMembers)
        val articleCardPksInFeedCards = articleCardService.findAllArticleCardInFeedCards(
            popularFeeds.map { it.pk }.toList()
        ).map { it.feedCardPk }.toSet()
        val filteredPopularFeeds = popularFeeds.filterNot { articleCardPksInFeedCards.contains(it.pk) }

        val feedLikes = feedLikeService.findByTargetCards(popularFeeds)
        val comments = commentCardService.findChildCommentsByParents(popularFeeds.map { it.pk })
        val pollVoterCntByFeedCardPk = pollVoteService.findVotedFeedCardPksByFeedCardPks(
            filteredPopularFeeds.map { it.pk }
        ).groupingBy { it }.eachCount()

        return filteredPopularFeeds.map {
            feedMapper.toFeedResponse(
                it,
                comments,
                feedLikes,
                DistanceDisplayUtil.calculateAndFormat(it.location, latitude, longitude),
                userId,
                pollVoterCntByFeedCardPk[it.pk]?.toLong() ?: 0L
            )
        }.filterNot { feedResponse ->
            feedResponse.isAdminCard && articleCardService.isArticleCard(feedResponse.cardId)
        }
    }
}