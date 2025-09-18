package com.clip.api.member.controller

import com.clip.api.docs.member.MemberDocs
import com.clip.api.member.controller.dto.*
import com.clip.api.member.service.MemberUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberUseCase: MemberUseCase
): MemberDocs {

    @PatchMapping("/fcm")
    override fun updateFCMToken(
        @RequestBody fcmRequest: FCMRequest,
        @AccessUser userId: Long
    ): ResponseEntity<Void> =
        memberUseCase.updateFCMToken(fcmRequest.fcmToken, userId)
            .let { ResponseEntity.ok().build() }


    @PostMapping("/check-available")
    override fun checkAvailableSignUp(@RequestBody checkAvailableRequest: CheckAvailableRequest): ResponseEntity<CheckAvailableResponse> {
        val response = memberUseCase.checkAvailableSignUp(checkAvailableRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/validate-nickname")
    override fun validateNickname(@RequestBody nicknameDto: NicknameDto): ResponseEntity<NicknameValidateResponse> {
        val response = memberUseCase.validateNickname(nicknameDto)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/nickname")
    override fun updateNickname(@RequestBody nicknameDto: NicknameDto, @AccessUser id: Long): ResponseEntity<Void> {
        memberUseCase.updateNickname(nicknameDto, id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/profile-img")
    override fun updateProfileImage(@RequestBody profileImageDto: ProfileImageDto, @AccessUser id: Long): ResponseEntity<Void> {
        memberUseCase.updateProfileImage(profileImageDto, id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/generate-nickname")
    override fun generateNickname(): ResponseEntity<NicknameDto> =
        ResponseEntity.ok(memberUseCase.generateNickname())
}