package com.clip.data.notice.service;

import com.clip.data.notice.entity.Notice;
import com.clip.data.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> findNoticesForNotification(Long lastPk, Integer pageSize) {
        if (pageSize > 30) pageSize = 30;
        LocalDate thirtyDaysAgo = LocalDateTime.now().minusDays(30).toLocalDate();
        return noticeRepository.findNoticesForNotification(lastPk, thirtyDaysAgo, PageRequest.ofSize(pageSize));
    }

    public List<Notice> findNoticesForSettings(Long lastPk, Integer pageSize) {
        if (pageSize > 30) pageSize = 30;
        return noticeRepository.findNoticeForSettings(lastPk, PageRequest.ofSize(pageSize));
    }
}
