package com.clip.api.member.controller

import com.clip.api.docs.member.MemberCardDocs
import com.clip.api.member.controller.dto.CardContentsResponse
import com.clip.api.member.service.MemberFeedUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberCardController(
    private val memberFeedUseCase: MemberFeedUseCase,
): MemberCardDocs {

    @GetMapping("/{userId}/cards/feed", "/{userId}/cards/feed/{lastId}")
    override fun getUserFeedCards(
        @PathVariable userId: Long,
        @PathVariable(required = false) lastId: Long?,
    ): ResponseEntity<CardContentsResponse> =
        memberFeedUseCase.getUserFeedCards(lastId, userId)
            .takeIf { it.cardContents.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/me/cards/comment", "/me/cards/comment/{lastId}")
    override fun getMyCommentCards(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<CardContentsResponse> =
        memberFeedUseCase.getMyCommentCards(lastId, userId)
            .takeIf { it.cardContents.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()



//    @GetMapping("/feeds/{userId}", "/feeds/{userId}/{lastId}")
//    override fun getUserFeedCards(
//        @PathVariable userId: Long,
//        @PathVariable(required = false) lastId: Long?,
//    ): ResponseEntity<CardContentsResponse> =
//        cardUseCase.getUserFeedCards(lastId, userId)
//            .takeIf { it.cardContents.isNotEmpty() }
//            ?.let { ResponseEntity.ok(it) }
//            ?: ResponseEntity.noContent().build()



//    @GetMapping("/comments", "/comments/{lastId}")
//    override fun getMyCommentCards(
//        @PathVariable(required = false) lastId: Long?,
//        @AccessUser userId: Long
//    ): ResponseEntity<CardContentsResponse> =
//        cardUseCase.getMyCommentCards(lastId, userId)
//            .takeIf { it.cardContents.isNotEmpty() }
//            ?.let { ResponseEntity.ok(it) }
//            ?: ResponseEntity.noContent().build()
}