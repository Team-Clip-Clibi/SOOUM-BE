package com.clip.data.card.repository;

import java.util.List;

public interface FeedCardCommentViewerNotifyHistoryBulkRepository {
    void bulkUpsertNotifyHistories(Long feedCardPk, Long commentWriterPk, List<Long> recipientPks);
}
