package com.clip.data.poll.repository;

import com.clip.data.poll.entity.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PollVoteRepository extends JpaRepository<PollVote, Long>, PollVoteInsertRepository {
    @Query("select pv.pollOption.pk from PollVote pv where pv.feedPoll.pk = :feedPollPk")
    List<Long> findPollOptionPksByFeedPollPk(@Param("feedPollPk") Long feedPollPk);

    @Query("select pv.pollOption.pk from PollVote pv " +
            "where pv.feedPoll.pk = :feedPollPk and pv.voter.pk = :voterPk")
    List<Long> findVotedPollOptionPks(@Param("feedPollPk") Long feedPollPk, @Param("voterPk") Long voterPk);

    @Modifying
    @Transactional
    @Query("delete from PollVote pv " +
            "where pv.feedPoll.pk = :feedPollPk " +
            "and pv.pollOption.pk = :pollOptionPk " +
            "and pv.voter.pk = :voterPk")
    void deleteByFeedPollPkAndPollOptionPkAndVoterPk(
            @Param("feedPollPk") Long feedPollPk,
            @Param("pollOptionPk") Long pollOptionPk,
            @Param("voterPk") Long voterPk
    );
}
