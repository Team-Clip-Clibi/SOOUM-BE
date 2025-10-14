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
        memberUseCase.updateNickname(nicknameDto.nickname, id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/profile-img")
    override fun updateProfileImage(@RequestBody profileImageDto: ProfileImageDto, @AccessUser id: Long): ResponseEntity<Void> {
        memberUseCase.updateProfileImage(profileImageDto.imageName, id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/generate-nickname")
    override fun generateNickname(): ResponseEntity<NicknameDto> =
        ResponseEntity.ok(memberUseCase.generateNickname())

    @GetMapping("/permissions/posting")
    override fun getPostingPermissions(
        @AccessUser userId: Long
    ): ResponseEntity<PostingPermissionDto> =
        ResponseEntity.ok(memberUseCase.getPostingPermissions(userId))

    @GetMapping("/profile/info/me")
    override fun getMyProfileSummaryInfo(
        @AccessUser userId: Long
    ): ResponseEntity<MyProfileInfoResponse> =
        ResponseEntity.ok(memberUseCase.getMyProfileSummaryInfo(userId))

    @GetMapping("/profile/info/{profileOwnerId}")
    override fun getUserProfileSummaryInfo(
        @PathVariable profileOwnerId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<UserProfileInfoResponse> =
        ResponseEntity.ok(memberUseCase.getUserProfileSummaryInfo(profileOwnerId, userId))

    @PatchMapping("/profile/info/me")
    override fun updateMyProfileInfo(
        @RequestBody profileInfoRequest: ProfileInfoRequest,
        @AccessUser userId: Long
    ): ResponseEntity<Void> {
        memberUseCase.updateNicknameAndProfileImg(profileInfoRequest, userId)
        return ResponseEntity.ok().build()
    }
}