package com.clip.api.docs.follow

import com.clip.api.follow.controller.dto.FollowDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Member", description = "Member API")
interface FollowDocs {

    @Operation(summary = "팔로우 API", description = """
        - 사용자가 다른 사용자를 팔로우할 수 있도록 합니다.
    """)
    fun followMember(followRequest: FollowDto, userId: Long): ResponseEntity<Void>

    @Operation(summary = "언팔로우 API", description = """
        - 사용자가 다른 사용자를 언팔로우할 수 있도록 합니다.
    """)
    fun unfollowMember(followRequest: FollowDto, userId: Long): ResponseEntity<Void>
}