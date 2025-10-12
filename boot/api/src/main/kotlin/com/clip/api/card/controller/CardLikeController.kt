package com.clip.api.card.controller

import com.clip.api.card.service.CardLikeUseCase
import com.clip.api.docs.card.CardLikeDocs
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cards")
class CardLikeController(
    private val cardLikeUseCase: CardLikeUseCase,
): CardLikeDocs {


    @PostMapping("/{cardId}/like")
    override fun createCardLike(@PathVariable cardId: Long, @AccessUser userId: Long): ResponseEntity<Unit> {
        return cardLikeUseCase.createCardLike(cardId, userId)
            .let { ResponseEntity.ok().build() }
    }

    @DeleteMapping("/{cardId}/like")
    override fun deleteCardLike(@PathVariable cardId: Long, @AccessUser userId: Long): ResponseEntity<Unit> {
        return cardLikeUseCase.deleteCardLike(cardId, userId)
            .let { ResponseEntity.ok().build() }
    }


}