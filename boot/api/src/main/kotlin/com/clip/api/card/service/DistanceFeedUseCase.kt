package com.clip.api.card.service

import com.clip.api.card.controller.dto.FeedResponse
import com.clip.api.card.mapper.FeedMapper
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedCardService
import com.clip.data.card.service.FeedLikeService
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class DistanceFeedUseCase(
    private val blockMemberService: BlockMemberService,
    private val feedLikeService: FeedLikeService,
    private val commentCardService: CommentCardService,
    private val feedMapper: FeedMapper,
    private val feedCardService: FeedCardService,
) {

    fun findDistanceFeeds(
        latitude: Double,
        longitude: Double,
        distance: Double,
        lastId: Long?,
        userId: Long
    ): List<FeedResponse> {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val userLocation = GeometryFactory().createPoint(Coordinate(longitude, latitude))
        val distanceFeeds = feedCardService.findFeedsByDistance(Optional.ofNullable(lastId), userLocation, distance , blockedMembers)

        val feedLikes = feedLikeService.findByTargetCards(distanceFeeds)
        val comments = commentCardService.findCommentCardsIn(distanceFeeds)

        return distanceFeeds.map {
            feedMapper.toFeedResponse(
                it,
                comments,
                feedLikes,
                com.clip.api.card.util.DistanceDisplayUtil.calculateAndFormat(it.location, latitude, longitude)
            )
        }
    }
}