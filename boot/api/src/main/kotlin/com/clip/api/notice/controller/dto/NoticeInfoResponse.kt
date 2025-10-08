package com.clip.api.notice.controller.dto

import com.clip.data.notice.entity.noticetype.NoticeType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class NoticeInfoResponse(
    val notices: List<NoticeInfo>
)

data class NoticeInfo(
    val id: Long,
    val noticeType: NoticeType,
    val title: String,
    val url: String?,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val createdAt: LocalDateTime
)
