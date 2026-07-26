package com.clip.api.poll.controller.dto

import java.math.BigDecimal

data class PollVoteResponse(
    val feedCardId: Long,
    val pollId: Long,
    val totalVoterCnt: Long,
    val options: List<PollOptionVoteResultResponse>,
)

data class PollOptionVoteResultResponse(
    val pollOptionId: Long,
    val content: String,
    val voteCnt: Long,
    val votePercentage: BigDecimal,
    val isVoted: Boolean,
)
