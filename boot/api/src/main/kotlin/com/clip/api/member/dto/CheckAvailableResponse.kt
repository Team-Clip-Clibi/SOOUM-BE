package com.clip.api.member.dto

import java.time.LocalDateTime

data class CheckAvailableResponse(
    val rejoinAvailableAt: LocalDateTime?,
    val banned: Boolean,
    val withdrawn: Boolean,
    val registered: Boolean,
)