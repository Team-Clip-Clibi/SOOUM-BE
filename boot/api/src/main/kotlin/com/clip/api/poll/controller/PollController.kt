package com.clip.api.poll.controller

import com.clip.api.docs.poll.PollDocs
import com.clip.api.poll.controller.dto.PollVoteResponse
import com.clip.api.poll.service.PollVoteUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/polls")
class PollController(
    private val pollVoteUseCase: PollVoteUseCase,
) : PollDocs {

    @PostMapping("/options/{pollOptionId}/votes")
    override fun createPollVote(
        @PathVariable pollOptionId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<PollVoteResponse> =
        pollVoteUseCase.createPollVote(pollOptionId, userId)
            .let { ResponseEntity.ok(it) }

    @DeleteMapping("/options/{pollOptionId}/votes")
    override fun deletePollVote(
        @PathVariable pollOptionId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<Unit> =
        pollVoteUseCase.deletePollVote(pollOptionId, userId)
            .let { ResponseEntity.ok().build() }
}
