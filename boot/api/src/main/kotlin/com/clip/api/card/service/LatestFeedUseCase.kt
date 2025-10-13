package com.clip.api.card.service

import com.clip.api.card.controller.dto.FeedCardResponse
import com.clip.api.card.mapper.FeedMapper
import com.clip.api.card.util.DistanceDisplayUtil
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedCardService
import com.clip.data.card.service.FeedLikeService
import org.springframework.stereotype.Service
import java.util.*

@Service
class LatestFeedUseCase(
    private val blockMemberService: BlockMemberService,
    private val feedLikeService: FeedLikeService,
    private val commentCardService: CommentCardService,
    private val feedMapper: FeedMapper,
    private val feedCardService: FeedCardService,
)  {

    fun findLatestFeeds(
        latitude: Double?,
        longitude: Double?,
        lastId: Long?,
        userId: Long
    ): List<FeedCardResponse> {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val latestFeeds = feedCardService.getLatestFeeds(Optional.ofNullable(lastId), blockedMembers)

        val feedLikes = feedLikeService.findByTargetCards(latestFeeds)
        val comments = commentCardService.findCommentCardsIn(latestFeeds.map { it.pk })

        return latestFeeds.map {
            feedMapper.toFeedResponse(
                it,
                comments,
                feedLikes,
                DistanceDisplayUtil.calculateAndFormat(it.location, latitude, longitude))
        }
    }
}