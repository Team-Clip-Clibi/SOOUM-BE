package com.clip.api.card.controller

import com.clip.api.card.controller.dto.CardResponse
import com.clip.api.card.controller.dto.FeedCardResponse
import com.clip.api.card.service.DistanceFeedUseCase
import com.clip.api.card.service.LatestFeedUseCase
import com.clip.api.card.service.PopularFeedUseCase
import com.clip.api.docs.card.FeedDocs
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/cards/feeds")
@RestController
class FeedController(
    private val popularFeedUseCase: PopularFeedUseCase,
    private val latestFeedUseCase: LatestFeedUseCase,
    private val distanceFeedUseCase: DistanceFeedUseCase,
): FeedDocs {

    @GetMapping("/popular")
    override fun getPopularFeed(
        @RequestParam(required = false) latitude: Double?,
        @RequestParam(required = false) longitude: Double?,
        @AccessUser userId: Long
    ): ResponseEntity<List<FeedCardResponse>> {
        val findPopularFeeds = popularFeedUseCase.findPopularFeeds(latitude, longitude, userId)
        if (findPopularFeeds.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(findPopularFeeds)
    }

    @GetMapping("/latest", "/latest/{lastId}")
    override fun getLatestFeed(
        @RequestParam(required = false) latitude: Double?,
        @RequestParam(required = false) longitude: Double?,
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<FeedCardResponse>> =
        latestFeedUseCase.findLatestFeeds(latitude, longitude, lastId, userId).takeIf { it.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/distance", "/distance/{lastId}")
    override fun getDistanceFeed(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam distance: Double,
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<FeedCardResponse>> =
        distanceFeedUseCase.findDistanceFeeds(latitude, longitude, distance, lastId, userId).takeIf { it.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

}