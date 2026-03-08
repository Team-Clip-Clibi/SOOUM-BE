package com.clip.api.notification.event

import com.clip.data.member.entity.Member

data class MultiFcmFollowerCardUploadEvent(
    val targetCardId: Long,
    val content: String,
    val nickname: String,
    val userImgUrl: String?,
    val recipientList: List<Member>,
)