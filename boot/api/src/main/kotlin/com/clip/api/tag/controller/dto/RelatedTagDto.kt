package com.clip.api.tag.controller.dto


data class RelatedTagRequest(
    val tag: String?,
)

data class TagInfoResponse(
    val tagInfos: List<TagInfo>,
)

data class TagInfo(
    val id: Long,
    val name: String,
    val usageCnt: Int,
)

data class FavoriteTagResponse(
    val favoriteTags: List<FavoriteTagInfo>,
)

data class FavoriteTagInfo(
    val id: Long,
    val name: String,
)