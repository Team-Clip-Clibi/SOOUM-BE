package com.clip.api.card.controller

import com.clip.api.card.controller.dto.*
import com.clip.api.card.service.CardUseCase
import com.clip.api.docs.card.CardDocs
import com.clip.global.security.annotation.AccessUser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cards")
class CardController(
    private val cardUseCase: CardUseCase,
) : CardDocs {

    @PostMapping
    override fun createFeedCard(
        request: HttpServletRequest,
        @RequestBody createFeedCardRequest: CreateFeedCardRequest,
        @AccessUser userId: Long,
    ): ResponseEntity<Unit> =
        cardUseCase.createFeedCard(request, createFeedCardRequest, userId)
            .let { ResponseEntity.ok().build() }


    @PostMapping("/{cardId}")
    override fun createCommentCard(
        request: HttpServletRequest,
        @PathVariable cardId: Long,
        @RequestBody createCommentCardRequest: CreateCommentCardRequest,
        @AccessUser userId: Long,
    ): ResponseEntity<Unit> =
        cardUseCase.createCommentCard(request, createCommentCardRequest, cardId, userId)
            .let { ResponseEntity.ok().build() }

    @GetMapping("/{cardId}")
    override fun getCardDetail(
        @RequestParam latitude: Double?,
        @RequestParam longitude: Double?,
        @PathVariable cardId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<CardDetailResponse> {
        return cardUseCase.getFeedCardDetail(latitude, longitude, cardId, userId)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{cardId}/comments", "/{cardId}/comments/{lastId}")
    override fun getCommentCard(
        @RequestParam(required = false) latitude: Double?,
        @RequestParam(required = false) longitude: Double?,
        @PathVariable(required = false) lastId: Long?,
        @PathVariable cardId: Long,
        @AccessUser userId: Long): ResponseEntity<List<CommentCardResponse>> =
        cardUseCase.getCommentCard(latitude, longitude, lastId, cardId, userId)
            .takeIf { it.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @DeleteMapping("/{cardId}")
    override fun deleteCard(@PathVariable cardId: Long, @AccessUser userId: Long): ResponseEntity<Unit> =
        cardUseCase.deleteCard(cardId, userId)
            .let { ResponseEntity.ok().build() }

}