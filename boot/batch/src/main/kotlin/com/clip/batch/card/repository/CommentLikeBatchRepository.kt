package com.clip.batch.card.repository

import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.CommentLike
import com.clip.data.card.repository.CommentLikeRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentLikeBatchRepository : CommentLikeRepository {

    @Query("select cl from CommentLike cl where cl.targetCard in :targetCardList")
    fun findCommentLikesForDeletion(@Param("targetCardList") targetCardList: List<CommentCard>): List<CommentLike>
}
