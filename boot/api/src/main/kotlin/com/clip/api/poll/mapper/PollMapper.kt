package com.clip.api.poll.mapper

import com.clip.api.poll.controller.dto.PollOptionVoteResultResponse
import com.clip.api.poll.controller.dto.PollVoteResponse
import com.clip.data.poll.entity.FeedPoll
import com.clip.data.poll.entity.PollOption
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class PollMapper {
    fun toPollVoteResponse(
        feedPoll: FeedPoll,
        pollOptions: List<PollOption>,
        voteCntByOption: Map<Long, Int>,
        votedOptionPks: Set<Long>,
        totalVoterCnt: Long,
    ): PollVoteResponse =
        PollVoteResponse(
            feedCardId = feedPoll.feedCard.pk,
            pollId = feedPoll.pk,
            totalVoterCnt = totalVoterCnt,
            options = pollOptions.map { pollOption ->
                val voteCnt = voteCntByOption[pollOption.pk]?.toLong() ?: 0L
                toPollOptionVoteResultResponse(
                    pollOption = pollOption,
                    voteCnt = voteCnt,
                    votePercentage = calculateVotePercentage(voteCnt, totalVoterCnt),
                    isVoted = votedOptionPks.contains(pollOption.pk),
                )
            }
        )

    private fun toPollOptionVoteResultResponse(
        pollOption: PollOption,
        voteCnt: Long,
        votePercentage: BigDecimal,
        isVoted: Boolean,
    ): PollOptionVoteResultResponse =
        PollOptionVoteResultResponse(
            pollOptionId = pollOption.pk,
            content = pollOption.content,
            voteCnt = voteCnt,
            votePercentage = votePercentage,
            isVoted = isVoted,
        )

    private fun calculateVotePercentage(voteCnt: Long, totalVoterCnt: Long): BigDecimal =
        if (totalVoterCnt == 0L) {
            BigDecimal.ZERO.setScale(2)
        } else {
            BigDecimal.valueOf(voteCnt)
                .multiply(HUNDRED)
                .divide(BigDecimal.valueOf(totalVoterCnt), 2, RoundingMode.HALF_UP)
        }

    companion object {
        private val HUNDRED = BigDecimal.valueOf(100)
    }
}
