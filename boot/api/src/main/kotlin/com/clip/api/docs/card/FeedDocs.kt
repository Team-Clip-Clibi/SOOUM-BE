package com.clip.api.docs.card

import com.clip.api.card.controller.dto.FeedResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Feed", description = "피드 조회 API")
interface FeedDocs {
    @Operation(summary = "로그인 API", description = """
        - 사용자가 로그인할 수 있도록 합니다. 가입 가능 여부 확인 API에서 가입이 되어있는 사용자로 확인된 경우에만 로그인이 가능합니다.
        - 가입이 되어있는 사용자 : 가입 가능 여부 확인 API에서 banned, withdrawn, registered 값이 모두 false인 사용자
        - accessToken과 refreshToken을 발급합니다.
    """)
    fun getPopularFeed(latitude: Double?, longitude: Double?, userId: Long): ResponseEntity<List<FeedResponse>>
}