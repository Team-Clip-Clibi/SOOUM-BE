package com.clip.api.member.controller

import com.clip.api.docs.member.MemberFollowDocs
import com.clip.api.member.controller.dto.FollowCountDto
import com.clip.api.member.controller.dto.FollowDto
import com.clip.api.member.controller.dto.FollowInfoDto
import com.clip.api.member.service.MemberFollowUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberFollowController(
    private val memberFollowUseCase: MemberFollowUseCase
) : MemberFollowDocs {

    @PostMapping("/follow")
    override fun followMember(followRequest: FollowDto, @AccessUser userId: Long): ResponseEntity<Void> =
        memberFollowUseCase.followMember(followRequest, userId).let { ResponseEntity.ok().build() }

    @DeleteMapping("/{toMemberId}/unfollow")
    override fun unfollowMember(@PathVariable toMemberId: Long, @AccessUser userId: Long): ResponseEntity<Void> =
        memberFollowUseCase.unfollowMember(toMemberId, userId).let { ResponseEntity.ok().build() }

    @GetMapping(
        "/{profileOwnerId}/following",
        "/{profileOwnerId}/following/{lastId}",
    )
    override fun getFollowingList(
        @PathVariable profileOwnerId: Long,
        @AccessUser userId: Long,
        @PathVariable(required = false) lastId: Long?
    ): ResponseEntity<List<FollowInfoDto>> = memberFollowUseCase
        .getFollowingList(profileOwnerId, userId, lastId)
        .takeIf { it.isNotEmpty() }
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.noContent().build()


    @GetMapping(
        "/{profileOwnerId}/followers",
        "/{profileOwnerId}/followers/{lastId}"
    )
    override fun getFollowerList(
        @PathVariable(required = false) profileOwnerId: Long,
        @AccessUser userId: Long,
        @PathVariable(required = false) lastId: Long?
    ): ResponseEntity<List<FollowInfoDto>> = memberFollowUseCase
        .getFollowerList(profileOwnerId, userId, lastId)
        .takeIf { it.isNotEmpty() }
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.noContent().build()
}