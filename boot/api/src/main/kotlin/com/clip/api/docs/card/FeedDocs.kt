package com.clip.api.docs.card

import com.clip.api.card.controller.dto.FeedResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Feed", description = "피드 조회 API")
interface FeedDocs {
    @Operation(summary = "인기 카드 피드 API", description = """
        - 인기 카드 피드 조회 API.
        - 해당 피드는 최대 200개의 카드 정보를 한번에 반환합니다. (최대 개수는 변경될 수 있습니다.)
        - 페이징을 지원하지 않습니다.
    """)
    fun getPopularFeed(latitude: Double?, longitude: Double?, userId: Long): ResponseEntity<List<FeedResponse>>
}