package com.clip.api.notification.event

import com.clip.data.member.entity.Member

data class MultiFcmFeedCardCommentViewersEvent(
    val commentContent: String,
    val targetCardId: Long,
    val recipientList: List<Member>
)
