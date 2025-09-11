package com.clip.api.member.controller

import com.clip.api.docs.member.MemberDocs
import com.clip.api.member.dto.CheckAvailableRequest
import com.clip.api.member.dto.CheckAvailableResponse
import com.clip.api.member.dto.NicknameValidateResponse
import com.clip.api.member.service.MemberUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberUserCase: MemberUseCase
): MemberDocs {

    @PostMapping("/check-available")
    override fun checkAvailableSignUp(@RequestBody checkAvailableRequest: CheckAvailableRequest): ResponseEntity<CheckAvailableResponse> {
        val response = memberUserCase.checkAvailableSignUp(checkAvailableRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/validate-nickname")
    override fun validateNickname(@RequestParam nickname: String): ResponseEntity<NicknameValidateResponse> {
        val response = memberUserCase.validateNickname(nickname)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/nickname")
    override fun updateNickname(@RequestParam nickname: String, @AccessUser id: Long): ResponseEntity<Void> {
        memberUserCase.updateNickname(nickname, id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/profile-img")
    override fun updateProfileImage(@RequestParam profileImg: String?, @AccessUser id: Long): ResponseEntity<Void> {
        memberUserCase.updateProfileImage(profileImg, id)
        return ResponseEntity.noContent().build()
    }
}