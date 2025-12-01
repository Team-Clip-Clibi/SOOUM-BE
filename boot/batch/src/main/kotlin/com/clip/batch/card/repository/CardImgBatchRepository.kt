package com.clip.batch.card.repository

import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.img.repository.CardImgRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface CardImgBatchRepository : CardImgRepository {

    @Modifying
    @Transactional
    @Query("update CardImg ci set ci.feedCard = null where ci.feedCard = :feedCard")
    fun updateFeedCardImgNull(@Param("feedCard") feedCard: FeedCard)

    @Modifying
    @Transactional
    @Query("update CardImg ci set ci.commentCard = null where ci.commentCard in :commentCards")
    fun updateCommentCardImgNull(@Param("commentCards") commentCards: List<CommentCard>)
}