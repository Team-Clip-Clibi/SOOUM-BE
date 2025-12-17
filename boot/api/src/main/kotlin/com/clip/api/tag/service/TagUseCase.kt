package com.clip.api.tag.service

import com.clip.api.tag.controller.dto.*
import com.clip.data.block.service.BlockMemberService
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.member.service.MemberService
import com.clip.data.tag.entity.FavoriteTag
import com.clip.data.tag.service.FavoriteTagService
import com.clip.data.tag.service.FeedTagService
import com.clip.data.tag.service.TagService
import com.clip.global.exception.IllegalStateException
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TagUseCase(
    private val tagService: TagService,
    private val memberService: MemberService,
    private val feedTagService: FeedTagService,
    private val blockMemberService: BlockMemberService,
    private val s3ImgService: S3ImgService,
    private val favoriteTagService: FavoriteTagService
) {
    companion object
    {
        const val MAX_RESULT_CNT = 20
        const val FAVORITE_TAG_LIMIT = 9
    }

    fun findRelatedTags(relatedTag: String, resultCnt: Int): TagInfoResponse =
        tagService.findRelatedTags(relatedTag, resultCnt.coerceIn(0, MAX_RESULT_CNT))
            .map { TagInfo(id = it.pk, name = it.content, usageCnt = it.count) }
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
            TagInfo(id = it.pk, name = it.content, usageCnt = it.count)
        }.let(::TagInfoResponse)


    fun findFeedTagCards(tagId: Long, lastId: Long?, userId: Long): TagCardContentsResponse {
        val blockedMembers = blockMemberService.findAllBlockMemberPks(userId)
        val isFavorite = favoriteTagService.isExistsByTagPkAndMemberPk(tagId, userId)

        val maxCardCount = 30
        val maxFetchCount = 10

        val cardContentMap = LinkedHashMap<Long, CardContent>()
        var lastFetchedCardId: Long? = lastId

        for (fetchTry in 0 until maxFetchCount) {
            if (cardContentMap.size >= maxCardCount) break

            val fetchedFeedTags = feedTagService.findFeedCardsByTag(
                tagId,
                Optional.ofNullable(lastFetchedCardId),
                blockedMembers
            )

            if (fetchedFeedTags.isEmpty()) break

            lastFetchedCardId = fetchedFeedTags.last().feedCard.pk

            for (feedTag in fetchedFeedTags) {
                if (cardContentMap.size >= maxCardCount) break

                val feedCard = feedTag.feedCard
                cardContentMap.putIfAbsent(
                    feedCard.pk,
                    CardContent(
                        cardId = feedCard.pk,
                        cardImgName = feedCard.imgName,
                        cardImgUrl = when (feedCard.imgType) {
                            CardImgType.DEFAULT ->
                                s3ImgService.generateDefaultCardImgUrl(feedCard.imgName)
                            CardImgType.USER ->
                                s3ImgService.generateUserCardImgUrl(feedCard.imgName)
                        },
                        cardContent = feedCard.content,
                        font = feedCard.font
                    )
                )
            }
        }

        return TagCardContentsResponse(
            cardContents = cardContentMap.values.take(maxCardCount),
            isFavorite = isFavorite
        )
    }

    fun findFavoriteTags(userId: Long): FavoriteTagResponse {
        val favoriteTags = favoriteTagService.findFavoriteTags(userId)
            .map {
                FavoriteTagInfo(
                    id = it.pk,
                    name = it.content
                )
            }
        return FavoriteTagResponse(favoriteTags)
    }

}