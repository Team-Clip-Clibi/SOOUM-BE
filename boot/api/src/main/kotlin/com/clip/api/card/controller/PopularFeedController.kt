package com.clip.api.card.controller

import com.clip.api.card.controller.dto.FeedResponse
import com.clip.api.card.service.PopularFeedUseCase
import com.clip.api.docs.card.FeedDocs
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/cards/feeds/popular")
@RestController
class PopularFeedController(
    private val popularFeedUseCase: PopularFeedUseCase
): FeedDocs {

    @GetMapping
    override fun getPopularFeed(
        @RequestParam(required = false) latitude: Double?,
        @RequestParam(required = false) longitude: Double?,
        @AccessUser userId: Long
    ): ResponseEntity<List<FeedResponse>> {
        val findPopularFeeds = popularFeedUseCase.findPopularFeeds(latitude, longitude, userId)
        if (findPopularFeeds.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(findPopularFeeds)
    }
}