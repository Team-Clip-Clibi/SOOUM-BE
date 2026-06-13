package com.clip.data.poll.service;

import com.clip.data.poll.entity.FeedPoll;
import com.clip.data.poll.entity.PollOption;
import com.clip.data.poll.repository.PollOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollOptionService {
    private final PollOptionRepository pollOptionRepository;

    @Transactional
    public void savePollOptions(FeedPoll feedPoll, List<String> pollContents) {
        List<PollOption> pollOptions = pollContents.stream()
                .map(content -> PollOption.builder()
                        .feedPoll(feedPoll)
                        .content(content)
                        .build()
                ).toList();
        pollOptionRepository.saveAll(pollOptions);
    }
}
