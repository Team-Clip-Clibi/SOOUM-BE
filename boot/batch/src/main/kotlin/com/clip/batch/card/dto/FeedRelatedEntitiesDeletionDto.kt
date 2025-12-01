package com.clip.batch.card.dto

import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.FeedLike
import com.clip.data.report.entity.FeedReport

data class FeedRelatedEntitiesDeletionDto(
    val feedCard: FeedCard,
    val feedLikes: List<FeedLike>,
    val feedReports: List<FeedReport>,
)