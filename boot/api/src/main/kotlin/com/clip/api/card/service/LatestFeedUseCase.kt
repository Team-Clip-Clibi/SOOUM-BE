package com.clip.api.card.service

import com.clip.api.card.controller.dto.FeedCardResponse
import com.clip.api.card.mapper.FeedMapper
import com.clip.api.card.util.DistanceDisplayUtil
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.service.ArticleCardService
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedCardService
import com.clip.data.card.service.FeedLikeService
import com.clip.data.poll.service.FeedPollService
import com.clip.data.poll.service.PollVoteService
import org.springframework.stereotype.Service
import java.util.*

@Service
class LatestFeedUseCase(
    private val blockMemberService: BlockMemberService,
    private val feedLikeService: FeedLikeService,
    private val commentCardService: CommentCardService,
    private val articleCardService: ArticleCardService,
    private val feedMapper: FeedMapper,
    private val feedCardService: FeedCardService,
    private val feedPollService: FeedPollService,
    private val pollVoteService: PollVoteService,
)  {

    fun findLatestFeeds(
        latitude: Double?,
        longitude: Double?,
        lastId: Long?,
        userId: Long
    ): List<FeedCardResponse> {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val latestFeeds = feedCardService.getLatestFeeds(
            Optional.ofNullable(lastId),
            blockedMembers
        )
        val articleCardPksInFeedCards = articleCardService.findAllArticleCardInFeedCards(
            latestFeeds.map { it.pk }.toList()
        ).map { it.feedCardPk }.toSet()
        val filteredLatestFeeds = latestFeeds.filterNot { articleCardPksInFeedCards.contains(it.pk) }


        val feedLikes = feedLikeService.findByTargetCards(filteredLatestFeeds)
        val comments = commentCardService.findChildCommentsByParents(filteredLatestFeeds.map { it.pk })
        val pollVoterCntByFeedCardPk = pollVoteService.findVotedFeedCardPksByFeedCardPks(
            filteredLatestFeeds.map { it.pk }
        ).groupingBy { it }.eachCount()
        val pollFeedCardPks = feedPollService.findFeedCardPksByFeedCardPks(
            filteredLatestFeeds.map { it.pk }
        ).toSet()

        return filteredLatestFeeds.map {
            feedMapper.toFeedResponse(
                it,
                comments,
                feedLikes,
                DistanceDisplayUtil.calculateAndFormat(it.location, latitude, longitude),
                userId,
                if (pollFeedCardPks.contains(it.pk)) pollVoterCntByFeedCardPk[it.pk]?.toLong() ?: 0L else null
            )
        }
    }
}
