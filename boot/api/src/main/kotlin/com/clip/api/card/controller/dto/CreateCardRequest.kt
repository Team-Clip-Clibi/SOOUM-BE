package com.clip.api.card.controller.dto

import com.clip.data.card.entity.font.Font
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.poll.entity.PollType
import com.clip.global.validation.NoBlankElements
import jakarta.validation.Valid
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

open class CreateFeedCardRequest(
    override val isDistanceShared: Boolean,
    override val latitude: Double?,
    override val longitude: Double?,
    override val content: String,
    override val font: Font,
    override val imgType: CardImgType,
    override val imgName: String,

    open val isStory: Boolean,
    open val tags: List<String>,
    open val isArticle: Boolean?,
    open val hasPoll: Boolean = false,
): CreateCardRequest(
    isDistanceShared = isDistanceShared,
    latitude = latitude,
    longitude = longitude,
    content = content,
    font = font,
    imgType = imgType,
    imgName = imgName
)

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
    override val hasPoll: Boolean,

    @field:Size(min = 2, max = 4)
    @field:Valid
    @field:NoBlankElements
    val pollContents: List<String>,
    val pollType: PollType,
) : CreateFeedCardRequest(isDistanceShared = isDistanceShared,
    latitude = latitude,
    longitude = longitude,
    content = content,
    font = font,
    imgType = imgType,
    imgName = imgName,
    isStory = isStory,
    tags = tags,
    isArticle = isArticle,
    hasPoll = hasPoll
)

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
