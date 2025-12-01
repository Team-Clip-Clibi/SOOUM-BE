package com.clip.batch.card.repository

import com.clip.data.card.entity.CommentCard
import com.clip.data.report.entity.CommentReport
import com.clip.data.report.repository.CommentReportRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentReportBatchRepository : CommentReportRepository {

    @Query("select cr from CommentReport cr where cr.targetCard in :targetCardList")
    fun findCommentReportsForDeletion(@Param("targetCardList") targetCardList: List<CommentCard>): List<CommentReport>
}
