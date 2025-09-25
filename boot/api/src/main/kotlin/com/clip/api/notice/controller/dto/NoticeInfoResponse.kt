package com.clip.api.notice.controller.dto

import java.time.LocalDate

data class NoticeInfoResponse(
    val notices: List<NoticeInfo>
)

data class NoticeInfo(
    val title: String,
    val url: String?,
    val createdAt: LocalDate
)
