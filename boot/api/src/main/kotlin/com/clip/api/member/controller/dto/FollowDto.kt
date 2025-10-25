package com.clip.api.member.controller.dto

data class FollowDto(
    val userId: Long
)

data class FollowInfoDto(
    val memberId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val isFollowing: Boolean,
    val isRequester: Boolean
)