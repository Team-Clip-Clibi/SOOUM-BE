package com.clip.api.member.dto

data class SignUpResponse(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
) {
}
