package com.clip.api.notice.controller.dto

import com.clip.data.notice.entity.noticetype.NoticeType
import java.time.LocalDate

data class NoticeInfoResponse(
    val notices: List<NoticeInfo>
)

data class NoticeInfo(
    val id: Long,
    val noticeType: NoticeType,
    val title: String,
    val url: String?,
    val createdAt: LocalDate
)
