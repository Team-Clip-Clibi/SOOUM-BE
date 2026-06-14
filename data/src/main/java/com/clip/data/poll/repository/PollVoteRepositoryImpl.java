package com.clip.data.poll.repository;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PollVoteRepositoryImpl implements PollVoteInsertRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertIfAbsentPollVote(Long feedPollPk, Long pollOptionPk, Long voterPk) {
        String sql = "INSERT INTO poll_vote " +
                "(pk, voter, feed_poll, poll_option, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, NOW(), NOW()) " +
                "ON DUPLICATE KEY UPDATE pk = pk";

        jdbcTemplate.update(
                sql,
                TSID.Factory.getTsid().toLong(),
                voterPk,
                feedPollPk,
                pollOptionPk
        );
    }
}
