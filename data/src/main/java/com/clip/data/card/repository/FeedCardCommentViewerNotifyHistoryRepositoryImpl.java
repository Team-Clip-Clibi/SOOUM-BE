package com.clip.data.card.repository;


import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import io.hypersistence.tsid.TSID;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedCardCommentViewerNotifyHistoryRepositoryImpl implements FeedCardCommentViewerNotifyHistoryBulkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void bulkUpsertNotifyHistories(Long feedCardPk, Long commentWriterPk, List<Long> recipientPks) {
        if (recipientPks == null || recipientPks.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO feed_card_comment_viewer_notify_history " +
                     "(pk, target_feed_card, recipient, comment_writer, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, NOW(), NOW()) " +
                     "ON DUPLICATE KEY UPDATE pk = pk";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                // 👉 바로 이 부분입니다!
                long generatedTsid = TSID.Factory.getTsid().toLong();

                ps.setLong(1, generatedTsid);
                ps.setLong(2, feedCardPk);
                ps.setLong(3, recipientPks.get(i));
                ps.setLong(4, commentWriterPk);
            }

            @Override
            public int getBatchSize() {
                return recipientPks.size();
            }
        });
    }
}