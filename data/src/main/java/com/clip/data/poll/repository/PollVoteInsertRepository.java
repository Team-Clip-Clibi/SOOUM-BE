package com.clip.data.poll.repository;

public interface PollVoteInsertRepository {
    void insertIfAbsentPollVote(Long feedPollPk, Long pollOptionPk, Long voterPk);
}
