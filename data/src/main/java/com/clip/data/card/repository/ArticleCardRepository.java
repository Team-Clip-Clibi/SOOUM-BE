package com.clip.data.card.repository;

import com.clip.data.card.entity.ArticleCard;
import com.clip.data.card.entity.FeedCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleCardRepository extends JpaRepository<ArticleCard, Long> {

    @Query("select f from FeedCard f " +
           "join ArticleCard a on f.pk = a.feedCardPk " +
           "join fetch f.writer " +
           "where f.isDeleted = false " +
           "and f.isFeedActive = true " +
           "order by a.pk desc " +
           "limit 1")
    Optional<FeedCard> findLatestArticleFeedCard();

    boolean existsByFeedCardPk(Long feedCardPk);
}

