package com.clip.data.card.repository;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.FeedView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedViewRepository extends JpaRepository<FeedView, Long> {

    @Query("SELECT COUNT(fv) FROM FeedView fv WHERE fv.targetFeed = :feedCard")
    Long countView(FeedCard feedCard);
}