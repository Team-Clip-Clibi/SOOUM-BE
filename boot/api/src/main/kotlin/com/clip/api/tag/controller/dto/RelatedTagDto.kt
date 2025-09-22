package com.clip.api.tag.controller.dto


data class RelatedTagRequest(
    val tag: String?,
)

data class TagInfoResponse(
    val tagInfos: List<TagInfo>,
)

data class TagInfo(
    val id: String,
    val name: String,
    val usageCnt: Int,
)
