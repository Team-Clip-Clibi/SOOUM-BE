package com.clip.api.docs.card

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
}