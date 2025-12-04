package com.clip.api.docs.card

import com.clip.api.card.controller.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity

@Tag(name = "Card", description = "카드 API")
interface CardDocs {

    @Operation(summary = "피드 카드 작성 API", description = """
        - 피드 카드 작성 API
    """)
    fun createFeedCard(
        request: HttpServletRequest,
        createFeedCardRequest: CreateFeedCardRequest,
        userId: Long,
    ): ResponseEntity<CreateCardResponse>

    @Operation(summary = "답카드 작성 API", description = """
        - 답카드 작성 API
    """)
    fun createCommentCard(
        request: HttpServletRequest,
        cardId: Long,
        createCommentCardRequest: CreateCommentCardRequest,
        userId: Long,
    ): ResponseEntity<CreateCardResponse>

    @Operation(summary = "카드 상세 조회 API", description = """
        - 카드 상세 조회 API
    """)
    fun getCardDetail(
        latitude: Double?,
        longitude: Double?,
        cardId: Long,
        userId: Long,
    ): ResponseEntity<CardDetailResponse>

    @Operation(summary = "답 카드 리스트 조회 API", description = """
        - 답 카드 리스트 조회 API
        - last 파라미터를 통해 페이징을 지원합니다. (last 파라미터는 이전 요청에서 반환된 카드 피드 리스트의 마지막 카드 ID입니다.)
    """)
    fun getCommentCard(
        latitude: Double?,
        longitude: Double?,
        lastId: Long?,
        cardId: Long,
        userId: Long,
    ): ResponseEntity<List<CommentCardResponse>>

    @Operation(summary = "카드 삭제 API", description = """
        - 카드 삭제 API
        - 피드 카드는 작성자만 삭제할 수 있습니다.
        - 답 카드는 답 카드 작성자만 삭제할 수 있습니다.
    """)
    fun deleteCard(
        cardId: Long,
        userId: Long,
    ): ResponseEntity<Unit>

    @Operation(summary = "카드 삭제여부 조회 API", description = """
        - 피드카드 id 또는 답카드 id로 해당 카드를 삭제할 수 있는지 조회하는 API
    """)
    fun isDeleteCard(
        cardId: Long,
    ): DeleteCheckResponse
}