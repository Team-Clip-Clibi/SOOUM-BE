package com.clip.api.member.controller.dto

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
) {
}
