package com.clip.batch.card.dto

import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.CommentLike
import com.clip.data.notification.entity.NotificationHistory
import com.clip.data.report.entity.CommentReport
import com.clip.data.tag.entity.CommentTag

data class CommentRelatedEntitiesDeletionDto(
    val commentCards: List<CommentCard>,
    val commentLikes: List<CommentLike>,
    val commentReports: List<CommentReport>,
    val notifications: List<NotificationHistory>,
    val commentTags: List<CommentTag>,
)
