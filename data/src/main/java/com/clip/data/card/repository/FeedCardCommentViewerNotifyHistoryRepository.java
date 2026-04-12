package com.clip.data.card.repository;

import com.clip.data.card.entity.FeedCardCommentViewerNotifyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedCardCommentViewerNotifyHistoryRepository extends JpaRepository<FeedCardCommentViewerNotifyHistory, Long>, FeedCardCommentViewerNotifyHistoryBulkRepository{
    @Query("select h.recipient.pk " +
            "from FeedCardCommentViewerNotifyHistory h " +
            "where h.targetFeedCard.pk = :feedCardPk " +
            "and h.commentWriter.pk = :commentWriterPk " +
            "and h.recipient.pk in :recipientPks")
    List<Long> findRecipientPksAlreadyNotifiedByFeedCardAndCommentWriter(
            @Param("feedCardPk") Long feedCardPk,
            @Param("commentWriterPk") Long commentWriterPk,
            @Param("recipientPks") List<Long> recipientPks
    );
}
