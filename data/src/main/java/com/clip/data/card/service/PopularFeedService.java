package com.clip.data.card.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.PopularFeed;
import com.clip.data.card.repository.PopularFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PopularFeedService {
    private final PopularFeedRepository popularFeedRepository;

    public void deletePopularCard(Long cardId) {
        popularFeedRepository.deletePopularCard(cardId);
    }

    public List<FeedCard> getPopularFeeds(List<Long> blockedMembers) {
        PageRequest pageRequest = PageRequest.ofSize(200);

        List<PopularFeed> popularFeeds = popularFeedRepository.findPopularFeeds(blockedMembers, pageRequest);
        return popularFeeds.stream()
                .map(PopularFeed::getPopularCard)
                .distinct()
                .toList();
    }

    public void deletePopularCardByMemberPk(Long memberPk) {
        popularFeedRepository.deletePopularCardByMemberPk(memberPk);
    }
}
