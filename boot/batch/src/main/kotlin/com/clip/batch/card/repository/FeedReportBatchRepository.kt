package com.clip.batch.card.repository

import com.clip.data.card.entity.FeedCard
import com.clip.data.report.entity.FeedReport
import com.clip.data.report.repository.FeedReportRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FeedReportBatchRepository : FeedReportRepository {

    @Query("select fr from FeedReport fr where fr.targetCard = :targetCardList")
    fun findFeedReportsForDeletion(
        @Param("targetCardList") targetCard: FeedCard
    ): List<FeedReport>
}