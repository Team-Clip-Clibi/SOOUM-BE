package com.clip.api.docs.member

import com.clip.api.member.controller.dto.CardContentsResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity

interface MemberCardDocs {
    @Operation(summary = "프로필 피드카드 조회 API", description = """
        - 조회할 유저의 userId를 통해 해당 유저가 작성한 피드 카드를 조회할 수 있습니다.
        - 조회할 데이터가 없으면 204 No Content를 반환합니다.
        - 한 페이지당 50개를 반환합니다.
    """)
    fun getUserFeedCards(
        userId: Long,
        lastId: Long?,
    ): ResponseEntity<CardContentsResponse>

    @Operation(summary = "내 프로필 답카드 조회 API", description = """
        - 내 프로필 화면에서 내가 작성한 답카드를 조회할 수 있습니다.
        - 조회할 데이터가 없으면 204 No Content를 반환합니다.
        - 한 페이지당 50개를 반환합니다.
    """)
    fun getMyCommentCards(
        lastId: Long?,
        userId: Long
    ): ResponseEntity<CardContentsResponse>
}