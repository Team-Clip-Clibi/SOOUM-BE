package com.clip.api.member.controller.dto

data class SignUpResponse(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
) {
}
