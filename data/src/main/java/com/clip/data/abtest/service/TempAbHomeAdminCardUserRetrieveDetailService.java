package com.clip.data.abtest.service;

import com.clip.data.abtest.entity.RetrieveType;
import com.clip.data.abtest.entity.TempAbHomeAdminCardUserGroup;
import com.clip.data.abtest.entity.TempAbHomeAdminCardUserRetrieveDetail;
import com.clip.data.abtest.repository.TempAbHomeAdminCardUserRetrieveDetailRepository;
import com.clip.data.card.entity.FeedCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//AB_TEST HomeAdminCardUserTest
@Service
@RequiredArgsConstructor
public class TempAbHomeAdminCardUserRetrieveDetailService {
    private final TempAbHomeAdminCardUserRetrieveDetailRepository tempAbHomeAdminCardUserRetrieveDetailRepository;

    @Transactional
    public void saveDisplayTypeLog(TempAbHomeAdminCardUserGroup userGroup, FeedCard feedCard) {
        tempAbHomeAdminCardUserRetrieveDetailRepository.save(
                TempAbHomeAdminCardUserRetrieveDetail.builder()
                        .adminCardUserGroup(userGroup)
                        .adminFeedCard(feedCard)
                        .retrieveType(RetrieveType.DISPLAY)
                        .build()
        );
    }

    @Transactional
    public void saveClickLog(TempAbHomeAdminCardUserGroup userGroup, FeedCard feedCard) {
        tempAbHomeAdminCardUserRetrieveDetailRepository.save(
                TempAbHomeAdminCardUserRetrieveDetail.builder()
                        .adminCardUserGroup(userGroup)
                        .adminFeedCard(feedCard)
                        .retrieveType(RetrieveType.CLICK)
                        .build()
        );
    }
}
