package com.clip.data.poll.repository;

import com.clip.data.poll.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
}
