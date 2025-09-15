package com.clip.data.notification.service;

import com.clip.data.member.entity.Member;
import com.clip.data.notification.entity.NotificationHistory;
import com.clip.data.notification.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHistoryService {
    private final NotificationHistoryRepository notificationHistoryRepository;

    @Transactional(readOnly = true)
    public Member findToMember(Long notificationId) {
        return notificationHistoryRepository.findToMember(notificationId);
    }
    @Transactional(readOnly = true)
    public List<NotificationHistory> findUnreadNotifications(Optional<Long> lastId, Long memberId) {

        return notificationHistoryRepository.findUnreadNotifications(
                memberId,
                lastId.orElse(null),
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findReadNotifications(Optional<Long> lastPk, Long memberId) {
        return notificationHistoryRepository.findReadNotifications(
                memberId,
                lastPk.orElse(null),
                LocalDateTime.now().minusDays(1),
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findUnreadCardNotifications(Optional<Long> lastPk, Long memberPk) {
        return notificationHistoryRepository.findUnreadCardNotifications(
                memberPk,
                lastPk.orElse(null),
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findReadCardNotifications(Optional<Long> lastPk, Long memberPk) {
        return notificationHistoryRepository.findReadCardNotifications(
                memberPk, lastPk.orElse(null),
                LocalDateTime.now().minusDays(1),
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findUnreadFollowNotifications(Optional<Long> lastPk, Long memberPk) {
        return notificationHistoryRepository.findUnreadFollowNotifications(
                lastPk.orElse(null),
                memberPk,
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findReadFollowNotifications(Optional<Long> lastPk, Long memberPk) {
        return notificationHistoryRepository.findReadFollowNotifications(
                lastPk.orElse(null),
                memberPk,
                LocalDateTime.now().minusDays(1),
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findUnreadNoticeNotifications(Optional<Long> lastPk, Long memberPk) {
        return notificationHistoryRepository.findUnreadNoticeNotifications(
                lastPk.orElse(null),
                memberPk,
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findReadNoticeNotifications(Optional<Long> lastPk, Long memberPk) {
        return notificationHistoryRepository.findReadNoticeNotifications(
                lastPk.orElse(null),
                memberPk,
                LocalDateTime.now().minusDays(1),
                PageRequest.ofSize(30)
        );
    }

    @Transactional
    public void updateToRead(Long notificationId) {
        notificationHistoryRepository.updateToRead(notificationId, LocalDateTime.now());
    }

    public void deleteNotification(Long targetCardPk) {
        notificationHistoryRepository.deleteNotification(targetCardPk);
    }

    @Transactional
    public Long save(NotificationHistory notificationHistory) {
        return notificationHistoryRepository.save(notificationHistory).getPk();
    }

    @Transactional
    public void deletePreviousBlockedHistories(Long memberPk) {
        notificationHistoryRepository.deletePreviousBlockedHistories(memberPk);
    }

    public void deleteAllNotificationHistory(Long memberPk) {
        notificationHistoryRepository.deleteAllNotificationHistory(memberPk);
    }
}
