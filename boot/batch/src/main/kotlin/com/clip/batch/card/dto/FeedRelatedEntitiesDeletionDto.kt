package com.clip.batch.card.dto

import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.FeedLike
import com.clip.data.report.entity.FeedReport
import com.clip.data.tag.entity.FeedTag

data class FeedRelatedEntitiesDeletionDto(
    val feedCard: FeedCard,
    val feedLikes: List<FeedLike>,
    val feedReports: List<FeedReport>,
    val feedTags: List<FeedTag>
)