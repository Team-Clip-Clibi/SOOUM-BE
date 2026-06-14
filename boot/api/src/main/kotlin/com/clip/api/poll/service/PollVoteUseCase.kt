package com.clip.api.poll.service

import com.clip.api.poll.controller.dto.PollVoteResponse
import com.clip.api.poll.mapper.PollMapper
import com.clip.data.member.service.MemberService
import com.clip.data.poll.entity.FeedPoll
import com.clip.data.poll.entity.PollVote
import com.clip.data.poll.service.PollOptionService
import com.clip.data.poll.service.PollVoteService
import com.clip.global.exception.IllegalStateException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PollVoteUseCase(
    private val memberService: MemberService,
    private val pollOptionService: PollOptionService,
    private val pollVoteService: PollVoteService,
    private val pollMapper: PollMapper,
) {
    @Transactional
    fun createPollVote(pollOptionId: Long, userId: Long): PollVoteResponse {
        val voter = memberService.findMember(userId)
        val selectedOption = pollOptionService.findWithFeedPoll(pollOptionId)
        val feedPoll = selectedOption.feedPoll

        return try {
        pollVoteService.save(PollVote(voter, feedPoll, selectedOption))
        getPollVoteResult(feedPoll, voter.pk)
        } catch (e: DataIntegrityViolationException) {
            throw IllegalStateException.AlreadyCompletedException()
        }
    }

    @Transactional
    fun deletePollVote(pollOptionId: Long, userId: Long) {
        val voter = memberService.findMember(userId)
        val selectedOption = pollOptionService.findWithFeedPoll(pollOptionId)
        val feedPoll = selectedOption.feedPoll

        pollVoteService.deletePollVote(feedPoll.pk, selectedOption.pk, voter.pk)
    }

    private fun getPollVoteResult(feedPoll: FeedPoll, voterPk: Long): PollVoteResponse {
        val pollOptions = pollOptionService.findByFeedPollPk(feedPoll.pk)
        val votedPollOptionPks = pollVoteService.findPollOptionPksByFeedPollPk(feedPoll.pk)
        val voteCntByOption = votedPollOptionPks.groupingBy { it }.eachCount()
        val votedOptionPks = pollVoteService.findVotedPollOptionPks(feedPoll.pk, voterPk).toSet()
        val totalVoterCnt = votedPollOptionPks.size.toLong()

        return pollMapper.toPollVoteResponse(
            feedPoll = feedPoll,
            pollOptions = pollOptions,
            voteCntByOption = voteCntByOption,
            votedOptionPks = votedOptionPks,
            totalVoterCnt = totalVoterCnt,
        )
    }
}
