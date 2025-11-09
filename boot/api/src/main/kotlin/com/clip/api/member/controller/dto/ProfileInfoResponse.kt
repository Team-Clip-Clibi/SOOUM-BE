package com.clip.api.member.controller.dto

data class MyProfileInfoResponse(
    val userId: Long,
    val nickname: String,
    val profileImgName: String?,
    val profileImageUrl: String?,
    val totalVisitCnt: Long,
    val todayVisitCnt: Long,
    val cardCnt: Long,
    val followingCnt: Long,
    val followerCnt: Long
)

data class UserProfileInfoResponse(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val totalVisitCnt: Long,
    val todayVisitCnt: Long,
    val cardCnt: Long,
    val followingCnt: Long,
    val followerCnt: Long,
    val isAlreadyFollowing: Boolean,
    val isBlocked: Boolean
)