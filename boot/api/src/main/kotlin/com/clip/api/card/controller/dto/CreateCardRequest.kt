package com.clip.api.card.controller.dto

import com.clip.data.card.entity.font.Font
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.poll.entity.PollType
import com.clip.global.validation.NoBlankElements
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Size

sealed class CreateCardRequest(
    open val isDistanceShared: Boolean,
    open val latitude: Double?,
    open val longitude: Double?,
    open val content: String,
    open val font: Font,
    open val imgType: CardImgType,
    open val imgName: String,
)

sealed interface FeedCardCreateRequest {
    val isDistanceShared: Boolean
    val latitude: Double?
    val longitude: Double?
    val content: String
    val font: Font
    val imgType: CardImgType
    val imgName: String
    val isStory: Boolean
    val tags: List<String>
    val isArticle: Boolean?
}

data class CreateFeedCardRequest(
    override val isDistanceShared: Boolean,
    override val latitude: Double?,
    override val longitude: Double?,
    override val content: String,
    override val font: Font,
    override val imgType: CardImgType,
    override val imgName: String,

    override val isStory: Boolean,
    override val tags: List<String>,
    override val isArticle: Boolean?,
): CreateCardRequest(
    isDistanceShared = isDistanceShared,
    latitude = latitude,
    longitude = longitude,
    content = content,
    font = font,
    imgType = imgType,
    imgName = imgName
), FeedCardCreateRequest

data class CreateFeedCardWithPollRequest(
    override val isDistanceShared: Boolean,
    override val latitude: Double?,
    override val longitude: Double?,
    override val content: String,
    override val font: Font,
    override val imgType: CardImgType,
    override val imgName: String,
    override val isStory: Boolean,
    override val tags: List<String>,
    override val isArticle: Boolean?,
    val hasPoll: Boolean = false,

    @field:Size(min = 2, max = 4)
    @field:Valid
    @field:NoBlankElements
    val pollContents: List<String>? = null,
    val pollType: PollType? = null,
) : FeedCardCreateRequest {
    @get:JsonIgnore
    @get:AssertTrue(message = "hasPoll이 true이면 pollContents와 pollType이 필요합니다")
    val isPollRequestValid: Boolean
        get() = !hasPoll || (pollContents != null && pollType != null)
}

data class CreateCommentCardRequest(
    override val isDistanceShared: Boolean,
    override val latitude: Double?,
    override val longitude: Double?,
    override val content: String,
    override val font: Font,
    override val imgType: CardImgType,
    override val imgName: String,

    val tags: List<String>,
): CreateCardRequest(
    isDistanceShared = isDistanceShared,
    latitude = latitude,
    longitude = longitude,
    content = content,
    font = font,
    imgType = imgType,
    imgName = imgName
)
