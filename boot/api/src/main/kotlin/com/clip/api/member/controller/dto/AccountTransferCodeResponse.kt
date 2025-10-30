package com.clip.api.member.controller.dto

import java.time.LocalDateTime

data class AccountTransferCodeResponse(
    val transferCode: String,
    val expiredAt: LocalDateTime
)
