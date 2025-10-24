package com.clip.api.follow.controller

import com.clip.api.docs.follow.FollowDocs
import com.clip.api.follow.controller.dto.FollowDto
import com.clip.api.follow.service.FollowUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/followers")
class FollowController(
    private val followUseCase: FollowUseCase
): FollowDocs {
    override fun followMember(followRequest: FollowDto, @AccessUser userId: Long): ResponseEntity<Void> =
        followUseCase.followMember(followRequest, userId).let { ResponseEntity.ok().build() }

    override fun unfollowMember(followRequest: FollowDto, @AccessUser userId: Long): ResponseEntity<Void> =
        followUseCase.unfollowMember(followRequest, userId).let { ResponseEntity.ok().build() }

}