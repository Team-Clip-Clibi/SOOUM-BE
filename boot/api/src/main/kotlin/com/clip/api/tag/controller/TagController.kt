package com.clip.api.tag.controller

import com.clip.api.docs.tag.TagDocs
import com.clip.api.tag.controller.dto.RelatedTagRequest
import com.clip.api.tag.controller.dto.TagInfoResponse
import com.clip.api.tag.service.TagUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagUseCase: TagUseCase,
): TagDocs {

    @GetMapping("/related/{resultCnt}")
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
}