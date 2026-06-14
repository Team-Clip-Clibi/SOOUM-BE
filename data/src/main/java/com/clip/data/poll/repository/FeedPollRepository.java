package com.clip.data.poll.repository;

import com.clip.data.poll.entity.FeedPoll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedPollRepository extends JpaRepository<FeedPoll, Long> {
    Optional<FeedPoll> findByFeedCardPk(Long feedCardPk);
}
