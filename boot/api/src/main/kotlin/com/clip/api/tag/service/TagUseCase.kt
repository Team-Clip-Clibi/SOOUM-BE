package com.clip.api.tag.service

import com.clip.api.tag.controller.dto.TagInfo
import com.clip.api.tag.controller.dto.TagInfoResponse
import com.clip.data.member.service.MemberService
import com.clip.data.tag.entity.FavoriteTag
import com.clip.data.tag.service.TagService
import com.clip.global.exception.IllegalStateException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagUseCase(
    private val tagService: TagService,
    private val memberService: MemberService,
) {
    companion object
    {
        const val MAX_RESULT_CNT = 20
        const val FAVORITE_TAG_LIMIT = 9
    }

    fun findRelatedTags(relatedTag: String, resultCnt: Int): TagInfoResponse =
        tagService.findRelatedTags(relatedTag, resultCnt.coerceIn(0, MAX_RESULT_CNT))
            .map { TagInfo(id = it.pk.toString(), name = it.content, usageCnt = it.count) }
            .let(::TagInfoResponse)

    @Transactional
    fun saveFavoriteTag(tagId: Long, userId: Long) {
        if (tagService.isExistFavoriteTag(tagId, userId)){
            throw IllegalStateException.AlreadyCompletedException()
        }
        if (tagService.findMyFavoriteTagsCount(userId) >= FAVORITE_TAG_LIMIT) {
            throw IllegalStateException.LimitOverException()
        }
        tagService.saveFavoriteTag(
            FavoriteTag(
                memberService.findMember(userId),
                tagService.findTag(tagId)
            )
        )
    }

    @Transactional
    fun deleteFavoriteTag(tagId: Long, userId: Long) {
        tagService.deleteFavoriteTag(
            tagService.findFavoriteTag(tagId, userId)
        )
    }

    fun findTop10Tags(): TagInfoResponse =
        tagService.findTop10Tags().map {
            TagInfo(id = it.pk.toString(), name = it.content, usageCnt = it.count)
        }.let(::TagInfoResponse)


}