package com.clip.api.member.controller.dto

data class AllowNotifyDto(
 val commentCardNotify: Boolean = true,
 val cardLikeNotify: Boolean = true,
 val followUserCardNotify: Boolean = true,
 val newFollowerNotify: Boolean = true,
 val cardNewCommentNotify: Boolean = true,
 val recommendedContentNotify: Boolean = true,
 val favoriteTagNotify: Boolean = true,
 val serviceUpdateNotify: Boolean = true,
 val policyViolationNotify: Boolean = true,
)
