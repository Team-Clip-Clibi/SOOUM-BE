package com.clip.api.card.mapper

import com.clip.api.card.controller.dto.PollOptionResponse
import com.clip.api.card.controller.dto.PollResponse
import com.clip.data.poll.entity.PollOption
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class CardPollMapper {
    fun toPollResponse(
        pollOptions: List<PollOption>,
        votedPollOptionPks: List<Long>,
        votedOptionPks: Set<Long>,
    ): PollResponse {
        val isPollVoted = votedOptionPks.isNotEmpty()
        val voteCntByOption = votedPollOptionPks.groupingBy { it }.eachCount()
        val totalVoterCnt = votedPollOptionPks.size.toLong()

        return PollResponse(
            totalVoterCnt = totalVoterCnt,
            isVoted = isPollVoted,
            options = pollOptions.map { pollOption ->
                val voteCnt = voteCntByOption[pollOption.pk]?.toLong() ?: 0L
                toPollOptionResponse(
                    pollOption = pollOption,
                    voteCnt = voteCnt,
                    votePercentage = calculateVotePercentage(voteCnt, totalVoterCnt),
                    isPollVoted = isPollVoted,
                    isOptionVoted = votedOptionPks.contains(pollOption.pk),
                )
            }
        )
    }

    private fun toPollOptionResponse(
        pollOption: PollOption,
        voteCnt: Long,
        votePercentage: BigDecimal,
        isPollVoted: Boolean,
        isOptionVoted: Boolean,
    ): PollOptionResponse =
        PollOptionResponse(
            pollOptionId = pollOption.pk,
            content = pollOption.content,
            voteCnt = voteCnt.takeIf { isPollVoted },
            votePercentage = votePercentage.takeIf { isPollVoted },
            isVoted = isOptionVoted,
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
