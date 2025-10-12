package com.clip.api.docs.card

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Card Like", description = "카드 좋아요 API")
interface CardLikeDocs {

    @Operation(summary = "카드 좋아요 API", description = """
        - 카드 좋아요 API
    """)
    fun createCardLike(
        cardId: Long,
        userId: Long,
    ): ResponseEntity<Unit>

    @Operation(summary = "카드 좋아요 취소 API", description = """
        - 카드 좋아요 취소 API
    """)
    fun deleteCardLike(
        cardId: Long,
        userId: Long,
    ): ResponseEntity<Unit>

}