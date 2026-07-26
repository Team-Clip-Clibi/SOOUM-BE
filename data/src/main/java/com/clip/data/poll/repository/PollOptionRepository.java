package com.clip.data.poll.repository;

import com.clip.data.poll.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    @Query("select po from PollOption po join fetch po.feedPoll fp join fetch fp.feedCard where po.pk = :pollOptionPk")
    Optional<PollOption> findWithFeedPoll(@Param("pollOptionPk") Long pollOptionPk);

    @Query("select po from PollOption po where po.feedPoll.pk = :feedPollPk order by po.pk asc")
    List<PollOption> findByFeedPollPk(@Param("feedPollPk") Long feedPollPk);

    @Query("select po from PollOption po where po.feedPoll.feedCard.pk = :feedCardPk order by po.pk asc")
    List<PollOption> findByFeedCardPk(@Param("feedCardPk") Long feedCardPk);
}
