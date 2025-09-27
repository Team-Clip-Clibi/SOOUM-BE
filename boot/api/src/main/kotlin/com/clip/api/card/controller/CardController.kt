package com.clip.api.card.controller

import com.clip.api.card.controller.dto.CreateCommentCardRequest
import com.clip.api.card.controller.dto.CreateFeedCardRequest
import com.clip.api.card.service.CardUseCase
import com.clip.global.security.annotation.AccessUser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cards")
class CardController(
    private val cardUseCase: CardUseCase,
) {

    @PostMapping
    fun createFeedCard(
        request: HttpServletRequest,
        @RequestBody createFeedCardRequest: CreateFeedCardRequest,
        @AccessUser userId: Long,
    ): ResponseEntity<Unit> =
        cardUseCase.createFeedCard(request, createFeedCardRequest, userId)
            .let { ResponseEntity.ok().build() }


    @PostMapping("/{cardId}")
    fun createCommentCard(
        request: HttpServletRequest,
        @PathVariable cardId: Long,
        @RequestBody createCommentCardRequest: CreateCommentCardRequest,
        @AccessUser userId: Long,
    ): ResponseEntity<Unit> =
        cardUseCase.createCommentCard(request, createCommentCardRequest, cardId, userId)
            .let { ResponseEntity.ok().build() }

}