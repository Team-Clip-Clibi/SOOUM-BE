package com.clip.data.report.repository;

import com.clip.data.report.entity.FeedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedReportRepository extends JpaRepository<FeedReport, Long> {

    @Query("select fr from FeedReport fr where fr.targetCard.pk = :cardPk")
    List<FeedReport> findByTargetCardPk(@Param("cardPk") Long cardPk);

    boolean existsByReporter_PkAndTargetCard_Pk(Long reporterPk, Long cardPk);

    @Modifying
    @Transactional
    @Query("delete from FeedReport f where f.targetCard.pk = :feedCardPk")
    void deleteAllByFeedCardPk(@Param("feedCardPk") Long feedCardPk);

    @Modifying
    @Transactional
    @Query("delete from FeedReport f where f.reporter.pk = :memberPk or f.targetCard.writer.pk = :memberPk")
    void deleteAllFeedReports(@Param("memberPk") Long memberPk);

    @Transactional
    @Modifying
    @Query("delete from FeedReport f where f.createdAt < :before6Month")
    void deleteAfter6Month(@Param("before6Month") LocalDateTime before6Month);
}
