package com.clip.data.notification.repository;

import com.clip.data.member.entity.Member;
import com.clip.data.notification.entity.NotificationHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    @Query("select n from NotificationHistory n " +
            "join fetch n.fromMember " +
            "where n.toMember.pk = :memberPk " +
                "and (:lastPk is null or n.pk < :lastPk) " +
                "and n.isRead = false " +
            "order by n.pk desc ")
    List<NotificationHistory> findUnreadNotifications(@Param("memberPk")Long memberPk, @Param("lastPk") Long lastPk, Pageable page);

    @Query("select n from NotificationHistory n " +
            "join fetch n.fromMember " +
            "where n.toMember.pk = :memberPk " +
                "and (:lastPk is null or n.pk < :lastPk) " +
                "and n.isRead = true " +
                "and n.readAt > :minusOneDays " +
            "order by n.pk desc ")
    List<NotificationHistory> findReadNotifications(@Param("memberPk") Long memberPk, @Param("lastPk") Long lastPk, @Param("minusOneDays") LocalDateTime minusOneDays, Pageable page);

    @Query("select n.toMember from NotificationHistory n where n.pk = :notificationPk")
    Member findToMember(@Param("notificationPk") Long notificationPk);

    @Transactional
    @Modifying
    @Query("update NotificationHistory n set n.isRead = true, n.readAt = :readAt where n.pk = :notificationPk")
    void updateToRead(@Param("notificationPk") Long notificationPk, @Param("readAt") LocalDateTime readAt);

    @Transactional
    @Modifying
    @Query("delete from NotificationHistory n where n.fromMember.pk = :memberPk or n.toMember.pk = :memberPk ")
    void deleteAllNotificationHistory(@Param("memberPk") Long memberPk);
}
