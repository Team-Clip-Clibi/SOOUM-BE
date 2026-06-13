package com.clip.data.poll.repository;

import com.clip.data.poll.entity.FeedPoll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedPollRepository extends JpaRepository<FeedPoll, Long> {
}
