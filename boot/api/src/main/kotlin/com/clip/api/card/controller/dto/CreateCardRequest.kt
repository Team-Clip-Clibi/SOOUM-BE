package com.clip.api.card.controller.dto

import com.clip.data.card.entity.font.Font
import com.clip.data.card.entity.imgtype.CardImgType

sealed class CreateCardRequest(
    open val isDistanceShared: Boolean,
    open val latitude: Double?,
    open val longitude: Double?,
    open val content: String,
    open val font: Font,
    open val imgType: CardImgType,
    open val imgName: String,
)

data class CreateFeedCardRequest(
    override val isDistanceShared: Boolean,
    override val latitude: Double?,
    override val longitude: Double?,
    override val content: String,
    override val font: Font,
    override val imgType: CardImgType,
    override val imgName: String,

    val isStory: Boolean,
    val tags: List<String>,
    val isArticle: Boolean?
): CreateCardRequest(
    isDistanceShared = isDistanceShared,
    latitude = latitude,
    longitude = longitude,
    content = content,
    font = font,
    imgType = imgType,
    imgName = imgName
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