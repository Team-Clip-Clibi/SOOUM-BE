package com.clip.data.card.repository;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.FeedView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedViewRepository extends JpaRepository<FeedView, Long> {

    @Query("SELECT COUNT(fv) FROM FeedView fv WHERE fv.targetFeed = :feedCard")
    Long countView(FeedCard feedCard);

    @Query(value = "SELECT EXISTS( " +
            "SELECT 1 FROM FeedView fv " +
            "WHERE fv.targetFeed.pk = :feedCardPk AND fv.visitor.pk = :visitorPk) ")
    boolean existsByFeedCardPkAndVisitorPk(
            @Param("feedCardPk") Long feedCardPk,
            @Param("visitorPk") Long visitorPk
    );
}