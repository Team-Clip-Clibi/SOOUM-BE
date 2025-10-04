package com.clip.api.member.controller.dto

import java.time.LocalDateTime

data class PostingPermissionDto(
    val isBaned: Boolean,
    val expiredAt: LocalDateTime?,
)
