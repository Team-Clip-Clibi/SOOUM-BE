package com.clip.api.docs.poll

import com.clip.api.poll.controller.dto.PollVoteResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Poll", description = "투표 API")
interface PollDocs {

    @Operation(summary = "투표하기 API", description = """
        - 선택한 투표 항목에 사용자 투표를 저장합니다.
        - 투표 완료 후 해당 피드 카드의 투표 결과를 반환합니다.
    """)
    fun createPollVote(
        pollOptionId: Long,
        userId: Long,
    ): ResponseEntity<PollVoteResponse>

    @Operation(summary = "투표 취소 API", description = """
        - 선택한 투표 항목에 저장된 사용자 투표를 삭제합니다.
    """)
    fun deletePollVote(
        pollOptionId: Long,
        userId: Long,
    ): ResponseEntity<Unit>
}
