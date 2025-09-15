package com.clip.data.card.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.tag.repository.FeedTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagFeedService {
    private final FeedTagRepository feedTagRepository;
    private static final int MAX_PAGE_SIZE = 50;

    public List<FeedCard> findTagFeeds(Long tagPk, Optional<Long> lastPk, List<Long> blockMemberPks) {
        PageRequest pageRequest = PageRequest.ofSize(MAX_PAGE_SIZE);
        return feedTagRepository.findFeeds(tagPk, lastPk.orElse(null), blockMemberPks, pageRequest);
    }
}
