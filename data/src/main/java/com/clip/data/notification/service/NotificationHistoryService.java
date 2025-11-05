package com.clip.data.notification.service;

import com.clip.data.member.entity.Member;
import com.clip.data.notification.entity.NotificationHistory;
import com.clip.data.notification.entity.notificationtype.NotificationType;
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
                LocalDateTime.now().minusDays(7),
                PageRequest.ofSize(30)
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationHistory> findReadNotifications(Optional<Long> lastPk, Long memberId) {
        return notificationHistoryRepository.findReadNotifications(
                memberId,
                lastPk.orElse(null),
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now().minusDays(30),
                PageRequest.ofSize(30)
        );
    }


    @Transactional
    public void updateToRead(Long notificationId) {
        notificationHistoryRepository.updateToRead(notificationId, LocalDateTime.now());
    }

    @Transactional
    public NotificationHistory save(NotificationHistory notificationHistory) {
        return notificationHistoryRepository.save(notificationHistory);
    }

    public void deleteAllNotificationHistory(Long memberPk) {
        notificationHistoryRepository.deleteAllNotificationHistory(memberPk);
    }

    @Transactional
    public void deletePreviousBlockedHistories(Long memberPk) {
        notificationHistoryRepository.deletePreviousBlockedHistories(memberPk);
    }

    public void deleteNotification(Long targetCardPk) {
        notificationHistoryRepository.deleteNotification(targetCardPk);
    }

    public NotificationHistory findNotificationHistoryByMemberAndType(Long fromMemberPk, Long toMemberPk, NotificationType notificationType) {
        return notificationHistoryRepository.findNotificationHistoryByMemberAndType(fromMemberPk, toMemberPk, notificationType).orElse(null);
    }

    @Transactional
    public void delete(NotificationHistory notificationHistory) {
        notificationHistoryRepository.delete(notificationHistory);
    }
}
