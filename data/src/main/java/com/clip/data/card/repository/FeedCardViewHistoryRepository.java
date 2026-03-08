package com.clip.data.card.repository;

import com.clip.data.card.entity.FeedCardViewHistory;
import com.clip.data.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedCardViewHistoryRepository extends JpaRepository<FeedCardViewHistory, Long> {
    @Query(value = "SELECT EXISTS( " +
            "SELECT 1 FROM FeedCardViewHistory h " +
            "WHERE h.viewer.pk = :viewerPk AND h.targetFeedCard.pk = :feedCardPk) ")
    boolean existsHistory(@Param("viewerPk") Long viewerPk, @Param("feedCardPk") Long feedCardPk);

    @Query("select h.viewer " +
            "from FeedCardViewHistory h " +
            "where h.targetFeedCard.pk = :feedCardPk " +
            "and h.viewer.pk <> :requesterPk " +
            "and h.viewer.pk <> :feedWriterPk " +
            "and h.viewer.cardNewCommentNotify = true " +
            "and h.viewer.firebaseToken is not null")
    List<Member> findNotifiableViewers(
            @Param("feedCardPk") Long feedCardPk,
            @Param("requesterPk") Long requesterPk,
            @Param("feedWriterPk") Long feedWriterPk
    );
}
