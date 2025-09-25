package com.clip.data.notice.service;

import com.clip.data.notice.entity.Notice;
import com.clip.data.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> findNotices(Long lastPk) {
        return noticeRepository.findNotice(lastPk, PageRequest.ofSize(30));
    }
}
