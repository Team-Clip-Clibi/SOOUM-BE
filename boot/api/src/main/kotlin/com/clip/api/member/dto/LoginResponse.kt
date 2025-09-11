package com.clip.api.member.dto

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
) {
}