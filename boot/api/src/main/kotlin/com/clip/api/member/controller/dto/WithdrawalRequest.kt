package com.clip.api.member.controller.dto

data class WithdrawalRequest(
    val accessToken: String,
    val refreshToken: String,
    val reason: String,
)
