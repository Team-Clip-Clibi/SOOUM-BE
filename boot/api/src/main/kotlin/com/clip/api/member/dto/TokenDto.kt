package com.clip.api.member.dto

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
) {
}
