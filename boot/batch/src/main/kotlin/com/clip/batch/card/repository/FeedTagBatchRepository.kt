package com.clip.batch.card.repository

import com.clip.data.card.entity.FeedCard
import com.clip.data.tag.entity.FeedTag
import com.clip.data.tag.repository.FeedTagRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FeedTagBatchRepository : FeedTagRepository {

    @Query("select ft from FeedTag ft where ft.feedCard = :targetCard")
    fun findFeedTagsForDeletion(
        @Param("targetCard") targetCard: FeedCard
    ): List<FeedTag>
}