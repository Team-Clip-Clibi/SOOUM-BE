package com.clip.api.docs.card

import com.clip.api.card.controller.dto.CardContentsResponse
import com.clip.api.card.controller.dto.CardDetailResponse
import com.clip.api.card.controller.dto.CommentCardResponse
import com.clip.api.card.controller.dto.CreateCommentCardRequest
import com.clip.api.card.controller.dto.CreateFeedCardRequest
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
    ): ResponseEntity<Unit>

    @Operation(summary = "답카드 작성 API", description = """
        - 답카드 작성 API
    """)
    fun createCommentCard(
        request: HttpServletRequest,
        cardId: Long,
        createCommentCardRequest: CreateCommentCardRequest,
        userId: Long,
    ): ResponseEntity<Unit>

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