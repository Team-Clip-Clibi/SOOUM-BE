package com.clip.api.tag.controller

import com.clip.api.docs.tag.TagDocs
import com.clip.api.tag.controller.dto.RelatedTagRequest
import com.clip.api.tag.controller.dto.TagCardContentsResponse
import com.clip.api.tag.controller.dto.TagInfoResponse
import com.clip.api.tag.service.TagUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagUseCase: TagUseCase,
): TagDocs {

    @PostMapping("/related/{resultCnt}")
    override fun getRelatedTags(
        @RequestBody relatedTagRequest: RelatedTagRequest,
        @PathVariable resultCnt: Int
    ): ResponseEntity<TagInfoResponse> =
        relatedTagRequest.tag
            ?.takeIf { it.isNotBlank() }
            ?.let { tag -> tagUseCase.findRelatedTags(tag, resultCnt) }
            ?.takeIf { it.tagInfos.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/rank")
    override fun getTop10Tags(): ResponseEntity<TagInfoResponse> =
        tagUseCase.findTop10Tags()
            .let { ResponseEntity.ok(it) }

    @PostMapping("/{tagId}/favorite")
    override fun createFavoriteTag(
        @PathVariable tagId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<Unit> =
        tagUseCase.saveFavoriteTag(tagId, userId)
            .let { ResponseEntity.ok().build() }

    @DeleteMapping("/{tagId}/favorite")
    override fun deleteFavoriteTag(
        @PathVariable tagId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<Unit> =
        tagUseCase.deleteFavoriteTag(tagId, userId)
            .let { ResponseEntity.ok().build() }

    @GetMapping("/{tagId}/cards", "/{tagId}/cards/{lastId}")
    override fun getTagFeedCards(@PathVariable tagId: Long, @PathVariable(required = false) lastId: Long?, @AccessUser userId: Long): ResponseEntity<TagCardContentsResponse> =
        tagUseCase.findFeedTagCards(tagId, lastId, userId)
            .takeIf { it.cardContents.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()


}