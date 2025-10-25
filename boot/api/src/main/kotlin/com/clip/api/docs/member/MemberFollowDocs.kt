package com.clip.api.docs.member

import com.clip.api.member.controller.dto.FollowDto
import com.clip.api.member.controller.dto.FollowInfoDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Member", description = "Member API")
interface MemberFollowDocs {

    @Operation(summary = "팔로우 API", description = """
        - 사용자가 다른 사용자를 팔로우할 수 있도록 합니다.
    """)
    fun followMember(followRequest: FollowDto, userId: Long): ResponseEntity<Void>

    @Operation(summary = "언팔로우 API", description = """
        - 사용자가 다른 사용자를 언팔로우할 수 있도록 합니다.
    """)
    fun unfollowMember(toMemberId: Long, userId: Long): ResponseEntity<Void>

    @Operation(summary = "팔로우 목록 조회 API", description = """
        - 사용자가 팔로우한 사용자 목록을 조회합니다.
    """)
    fun getFollowingList(profileOwnerId: Long, userId: Long, lastId: Long?): ResponseEntity<List<FollowInfoDto>>

    @Operation(summary = "팔로워 목록 조회 API", description = """
        - 사용자의 팔로워 목록을 조회합니다.
    """)
    fun getFollowerList(profileOwnerId: Long, userId: Long, lastId: Long?): ResponseEntity<List<FollowInfoDto>>

}