package com.clip.data.notification.repository;

import com.clip.data.member.entity.Member;
import com.clip.data.notification.entity.NotificationHistory;
import com.clip.data.notification.entity.notificationtype.NotificationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    @Query("select n from NotificationHistory n " +
            "join fetch n.fromMember " +
            "where n.toMember.pk = :memberPk " +
                "and (:lastPk is null or n.pk < :lastPk) " +
                "and n.isRead = false " +
                "and n.createdAt >= :minusSevenDays " +
            "order by n.pk desc ")
    List<NotificationHistory> findUnreadNotifications(
            @Param("memberPk")Long memberPk,
            @Param("lastPk") Long lastPk,
            @Param("minusSevenDays") LocalDateTime minusSevenDays,
            Pageable page
    );

    @Query("select n from NotificationHistory n " +
            "join fetch n.fromMember " +
            "where n.toMember.pk = :memberPk " +
            "and (:lastPk is null or n.pk < :lastPk) " +
            "and ((n.isRead = true and n.createdAt >= :minusThirtyDays) " +
                "or (n.isRead =  false and n.createdAt < :minusSevenDays and n.createdAt > :minusThirtyDays)) " +
            "order by n.pk desc ")
    List<NotificationHistory> findReadNotifications(
            @Param("memberPk") Long memberPk,
            @Param("lastPk") Long lastPk,
            @Param("minusSevenDays") LocalDateTime minusSevenDays,
            @Param("minusThirtyDays") LocalDateTime minusThirtyDays,
            Pageable page
    );

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

    @Transactional
    @Modifying
    @Query("delete from NotificationHistory n " +
            "where n.toMember.pk = :toMemberPk " +
            "and n.notificationType = com.clip.data.notification.entity.notificationtype.NotificationType.BLOCKED")
    void deletePreviousBlockedHistories(@Param("toMemberPk") Long toMemberPk);

    @Transactional
    @Modifying
    @Query("delete from NotificationHistory n where n.targetCardPk = :targetCardPk")
    void deleteNotification(@Param("targetCardPk") Long targetCardPk);

    @Query("select n from NotificationHistory n " +
           "where n.fromMember.pk = :fromMemberPk " +
           "and n.toMember.pk = :toMemberPk " +
           "and n.notificationType = :notificationType ")
    Optional<NotificationHistory> findNotificationHistoryByMemberAndType(Long fromMemberPk, Long toMemberPk, NotificationType notificationType);

}
