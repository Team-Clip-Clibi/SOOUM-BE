package com.clip.api.member.controller

import com.clip.api.docs.member.MemberFollowDocs
import com.clip.api.member.controller.dto.FollowDto
import com.clip.api.member.service.MemberFollowUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberFollowController(
    private val memberFollowUseCase: MemberFollowUseCase
): MemberFollowDocs {

    @PostMapping("/follow")
    override fun followMember(followRequest: FollowDto, @AccessUser userId: Long): ResponseEntity<Void> =
        memberFollowUseCase.followMember(followRequest, userId).let { ResponseEntity.ok().build() }

    @DeleteMapping("/{toMemberId}/unfollow")
    override fun unfollowMember(@PathVariable toMemberId: Long, @AccessUser userId: Long): ResponseEntity<Void> =
        memberFollowUseCase.unfollowMember(toMemberId, userId).let { ResponseEntity.ok().build() }

}