package com.clip.batch.card.repository

import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.repository.CommentCardRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param



interface CommentCardBatchRepository : CommentCardRepository {

    @Query("select cc from CommentCard cc where cc.masterCard in :feedCardPk")
    fun findCommentCardsForDeletion(@Param("feedCardPk") feedCardPk: Long): List<CommentCard>

    @Query("""
        select fc from CommentCard cc inner join FeedCard fc on fc.pk = cc.masterCard 
        where fc.isStory = false
          and fc.isFeedActive = true 
          and fc.createdAt >= (current_timestamp - 2 day) 
        group by cc.masterCard 
        having count(distinct cc.writer) >= 2
        order by count(fc.pk) desc
    """)
    fun findPopularCondFeedCards(pageable: Pageable): List<FeedCard>
}
