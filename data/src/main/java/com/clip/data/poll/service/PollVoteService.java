package com.clip.data.poll.service;

import com.clip.data.poll.repository.PollVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollVoteService {
    private final PollVoteRepository pollVoteRepository;

    public void insertIfAbsentPollVote(Long feedPollPk, Long pollOptionPk, Long voterPk) {
        pollVoteRepository.insertIfAbsentPollVote(feedPollPk, pollOptionPk, voterPk);
    }

    public void deletePollVote(Long feedPollPk, Long pollOptionPk, Long voterPk) {
        pollVoteRepository.deleteByFeedPollPkAndPollOptionPkAndVoterPk(feedPollPk, pollOptionPk, voterPk);
    }

    public List<Long> findPollOptionPksByFeedPollPk(Long feedPollPk) {
        return pollVoteRepository.findPollOptionPksByFeedPollPk(feedPollPk);
    }

    public List<Long> findVotedPollOptionPks(Long feedPollPk, Long voterPk) {
        return pollVoteRepository.findVotedPollOptionPks(feedPollPk, voterPk);
    }
}
