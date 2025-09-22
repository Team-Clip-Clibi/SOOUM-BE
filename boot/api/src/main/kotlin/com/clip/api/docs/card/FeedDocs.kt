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

    @Operation(summary = "최신 카드 피드 API", description = """
        - 최신 카드 피드 조회 API.
        - 해당 피드는 최대 20개의 카드 정보를 한번에 반환합니다. (최대 개수는 변경될 수 있습니다.)
        - last 파라미터를 통해 페이징을 지원합니다. (last 파라미터는 이전 요청에서 반환된 카드 피드 리스트의 마지막 카드 ID입니다.)
    """)
    fun getLatestFeed(latitude: Double?, longitude: Double?, lastId: Long?, userId: Long): ResponseEntity<List<FeedResponse>>

    @Operation(summary = "거리순 카드 피드 API", description = """
        - 거리순 카드 피드 조회 API.
        - 해당 피드는 최대 20개의 카드 정보를 한번에 반환합니다. (최대 개수는 변경될 수 있습니다.)
        - 거리는 km 단위로 보내주세요. (예: 1km -> 1.0)
        - last 파라미터를 통해 페이징을 지원합니다. (last 파라미터는 이전 요청에서 반환된 카드 피드 리스트의 마지막 카드 ID입니다.)
    """)
    fun getDistanceFeed(latitude: Double, longitude: Double, distance: Double, lastId: Long?, userId: Long): ResponseEntity<List<FeedResponse>>
}