package com.clip.api.card.service

import com.clip.api.card.controller.dto.FeedResponse
import com.clip.api.card.mapper.FeedMapper
import com.clip.api.card.util.DistanceDisplayUtil
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedLikeService
import com.clip.data.card.service.PopularFeedService
import org.springframework.stereotype.Service

@Service
class PopularFeedUseCase(
    private val popularFeedService: PopularFeedService,
    private val blockMemberService: BlockMemberService,
    private val feedLikeService: FeedLikeService,
    private val commentCardService: CommentCardService,
    private val feedMapper: FeedMapper,
) {

    fun findPopularFeeds(latitude: Double?, longitude: Double?, userId: Long): List<FeedResponse> {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val popularFeeds = popularFeedService.getPopularFeeds(blockedMembers)

        val feedLikes = feedLikeService.findByTargetCards(popularFeeds)
        val comments = commentCardService.findCommentCardsIn(popularFeeds)

        return popularFeeds.map {
            feedMapper.toFeedResponse(
                it,
                comments,
                feedLikes,
                DistanceDisplayUtil.calculateAndFormat(it.location, latitude, longitude))
        }
    }
}