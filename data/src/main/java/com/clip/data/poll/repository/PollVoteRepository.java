package com.clip.data.poll.repository;

import com.clip.data.poll.entity.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
}
