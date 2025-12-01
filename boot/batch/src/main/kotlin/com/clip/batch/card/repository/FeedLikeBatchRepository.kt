package com.clip.batch.card.repository

import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.FeedLike
import com.clip.data.card.repository.FeedLikeRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FeedLikeBatchRepository : FeedLikeRepository {

    @Query("select fl from FeedLike fl where fl.targetCard = :targetCard")
    fun findFeedLikesForDeletion(
        @Param("targetCard") targetCard: FeedCard
    ): List<FeedLike>

    @Query(
        """
        select fl.targetCard from FeedLike fl inner join fl.targetCard f
        where f.isStory = false
          and f.isFeedActive = true 
          and fl.targetCard.createdAt >= (current_timestamp - 2 day)
        group by fl.targetCard.pk
          having count(fl.likedMember.pk) >= 2
        order by count(fl.targetCard.pk) desc
        """
    )
    fun findPopularCondFeedCards(pageable: Pageable): List<FeedCard>
}
