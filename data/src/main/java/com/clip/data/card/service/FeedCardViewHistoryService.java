package com.clip.data.card.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.FeedCardViewHistory;
import com.clip.data.card.repository.FeedCardViewHistoryRepository;
import com.clip.data.member.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCardViewHistoryService {
    private final FeedCardViewHistoryRepository feedCardViewHistoryRepository;
    private final EntityManager entityManager;

    @Transactional
    public void saveViewedFeedCard(Long viewerPk, Long feedCardPk) {
        if (viewerPk == null || feedCardPk == null) {
            return;
        }

        boolean alreadyViewed = feedCardViewHistoryRepository.existsHistory(viewerPk, feedCardPk);
        if (alreadyViewed) {
            return;
        }

        Member viewerRef = entityManager.getReference(Member.class, viewerPk);
        FeedCard feedCardRef = entityManager.getReference(FeedCard.class, feedCardPk);
        feedCardViewHistoryRepository.save(new FeedCardViewHistory(feedCardRef, viewerRef));
    }

    @Transactional(readOnly = true)
    public List<Member> findNotifiableViewers(Long feedCardPk, Long requesterPk, Long feedWriterPk) {
        return feedCardViewHistoryRepository.findNotifiableViewers(feedCardPk, requesterPk, feedWriterPk);
    }
}
