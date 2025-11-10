package com.clip.api.tag.service

import com.clip.api.tag.controller.dto.CardContent
import com.clip.api.tag.controller.dto.TagCardContentsResponse
import com.clip.api.tag.controller.dto.TagInfo
import com.clip.api.tag.controller.dto.TagInfoResponse
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.member.service.MemberService
import com.clip.data.tag.entity.FavoriteTag
import com.clip.data.tag.service.FeedTagService
import com.clip.data.tag.service.TagService
import com.clip.global.exception.IllegalStateException
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagUseCase(
    private val tagService: TagService,
    private val memberService: MemberService,
    private val feedTagService: FeedTagService,
    private val blockMemberService: BlockMemberService,
    private val s3ImgService: S3ImgService,
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


    fun findFeedTagCards(tagId: Long, userId: Long): TagCardContentsResponse {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val cardContents = feedTagService
            .findFeedCardsByTag(tagId, blockedMembers)
            .map {
                CardContent(
                    cardId = it.feedCard.pk,
                    cardImgName = it.feedCard.imgName,
                    cardImgUrl = when(it.feedCard.imgType){
                        CardImgType.DEFAULT -> s3ImgService.generateDefaultCardImgUrl(it.feedCard.imgName)
                        CardImgType.USER -> s3ImgService.generateUserCardImgUrl(it.feedCard.imgName)
                    },
                    cardContent = it.feedCard.content,
                    font = it.feedCard.font
                )
            }
        return TagCardContentsResponse(cardContents)
    }

}