package com.clip.api.tag.service

import com.clip.api.tag.controller.dto.TagInfo
import com.clip.api.tag.controller.dto.TagInfoResponse
import com.clip.data.tag.service.TagService
import org.springframework.stereotype.Service

@Service
class TagUseCase(
    private val tagService: TagService,
) {
    companion object
    {
        const val MAX_RESULT_CNT = 20
    }

    fun findRelatedTags(relatedTag: String, resultCnt: Int): TagInfoResponse =
        tagService.findRelatedTags(relatedTag, resultCnt.coerceIn(0, MAX_RESULT_CNT))
            .map { TagInfo(name = it.content, usageCnt = it.count) }
            .let(::TagInfoResponse)
}