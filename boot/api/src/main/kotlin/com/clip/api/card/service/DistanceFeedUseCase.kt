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
import org.geolatte.geom.jts.JTS
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class DistanceFeedUseCase(
    private val blockMemberService: BlockMemberService,
    private val feedLikeService: FeedLikeService,
    private val commentCardService: CommentCardService,
    private val articleCardService: ArticleCardService,
    private val feedMapper: FeedMapper,
    private val feedCardService: FeedCardService,
    private val feedPollService: FeedPollService,
    private val pollVoteService: PollVoteService,
) {

    fun findDistanceFeeds(
        latitude: Double,
        longitude: Double,
        distance: Double,
        lastId: Long?,
        userId: Long
    ): List<FeedCardResponse> {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val userLocation = GeometryFactory().createPoint(Coordinate(longitude, latitude))
        val distanceFeeds = feedCardService.findFeedsByDistance(Optional.ofNullable(lastId), userLocation, distance , blockedMembers)

        val articleCardPksInFeedCards = articleCardService.findAllArticleCardInFeedCards(
            distanceFeeds.map { it.pk }.toList()
        ).map { it.feedCardPk }.toSet()
        val filteredDistanceFeeds = distanceFeeds.filterNot { articleCardPksInFeedCards.contains(it.pk) }

        val feedLikes = feedLikeService.findByTargetCardIds(distanceFeeds.map { it.pk })
        val comments = commentCardService.findChildCommentsByParents(distanceFeeds.map { it.pk })
        val pollVoterCntByFeedCardPk = pollVoteService.findVotedFeedCardPksByFeedCardPks(
            filteredDistanceFeeds.map { it.pk }
        ).groupingBy { it }.eachCount()
        val pollFeedCardPks = feedPollService.findFeedCardPksByFeedCardPks(
            filteredDistanceFeeds.map { it.pk }
        ).toSet()

        return filteredDistanceFeeds.map {
            feedMapper.toFeedResponse(
                it,
                comments,
                feedLikes,
                DistanceDisplayUtil.calculateAndFormat(JTS.to(it.location), latitude, longitude),
                userId,
                if (pollFeedCardPks.contains(it.pk)) pollVoterCntByFeedCardPk[it.pk]?.toLong() ?: 0L else null
            )
        }
    }
}