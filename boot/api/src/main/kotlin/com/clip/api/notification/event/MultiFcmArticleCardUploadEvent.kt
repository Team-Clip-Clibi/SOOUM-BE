package com.clip.api.notification.event

import com.clip.data.member.entity.Member

data class MultiFcmArticleCardUploadEvent(
    val targetCardId: Long,
    val content: String,
    val userImgUrl: String?,
    val recipientList: List<Member>,
)
