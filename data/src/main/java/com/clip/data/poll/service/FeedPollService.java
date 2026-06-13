package com.clip.data.poll.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.poll.entity.FeedPoll;
import com.clip.data.poll.entity.PollType;
import com.clip.data.poll.repository.FeedPollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedPollService {
    private final FeedPollRepository feedPollRepository;

    @Transactional
    public FeedPoll saveFeedPoll(FeedCard feedCard, PollType pollType) {
        return feedPollRepository.save(FeedPoll.builder()
                .feedCard(feedCard)
                .pollType(pollType)
                .build());
    }
}
