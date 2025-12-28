package com.clip.data.notification.service;

import com.clip.data.notification.entity.FcmSchedulerContent;
import com.clip.data.notification.repository.FcmSchedulerContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmSchedulerContentService {

    private final FcmSchedulerContentRepository fcmSchedulerContentRepository;

    public FcmSchedulerContent findFirstSchedulerContent() {
        return fcmSchedulerContentRepository.findByPk(1L).orElseThrow();
    }

    public FcmSchedulerContent findSecondSchedulerContent() {
        return fcmSchedulerContentRepository.findByPk(2L).orElseThrow();
    }
}
