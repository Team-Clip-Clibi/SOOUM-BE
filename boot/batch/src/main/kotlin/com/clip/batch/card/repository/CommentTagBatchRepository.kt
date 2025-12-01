package com.clip.batch.card.repository

import com.clip.data.card.entity.CommentCard
import com.clip.data.tag.entity.CommentTag
import com.clip.data.tag.repository.CommentTagRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentTagBatchRepository : CommentTagRepository {

    @Query("select ct from CommentTag ct where ct.commentCard in :commentCards")
    fun findCommentTagsForDeletion(
        @Param("commentCards") commentCards: List<CommentCard>
    ): List<CommentTag>
}
