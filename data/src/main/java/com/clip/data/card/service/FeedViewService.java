package com.clip.data.card.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.FeedView;
import com.clip.data.card.repository.FeedViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedViewService {
    private final FeedViewRepository feedViewRepository;

    public void save(FeedView feedView) {
        feedViewRepository.save(feedView);
    }

    public Long countView(FeedCard feedCard) {
        return feedViewRepository.countView(feedCard);
    }
}
