package com.clip.data.poll.repository;

import com.clip.data.poll.entity.FeedPoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedPollRepository extends JpaRepository<FeedPoll, Long> {
    Optional<FeedPoll> findByFeedCardPk(Long feedCardPk);

    @Query("select fp.feedCard.pk from FeedPoll fp where fp.feedCard.pk in :feedCardPks")
    List<Long> findFeedCardPksByFeedCardPks(@Param("feedCardPks") List<Long> feedCardPks);
}
