package com.clip.api.member.service

import com.clip.api.member.dto.CheckAvailableRequest
import com.clip.api.member.dto.CheckAvailableResponse
import com.clip.api.member.dto.NicknameValidateResponse
import com.clip.data.img.service.ProfileImgService
import com.clip.data.member.service.MemberService
import com.clip.data.member.service.SuspendedService
import com.clip.global.util.BadWordFilter
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3ImgPathProperties
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberUseCase(
    private val suspendedService: SuspendedService,
    private val memberService: MemberService,
    private val decodeWithRsa: DecodeWithRsa,
    private val s3ImgService: S3ImgService,
    private val s3ImgPathProperties: S3ImgPathProperties,
    private val rekognitionService: RekognitionService,
    private val profileImgService: ProfileImgService,
) {
    companion object {
        private val FORBIDDEN_NICKNAME = listOf("숨 운영자", "숨 운영진", "숨 관리자", "숨 관리진", "운영자", "운영진", "관리자", "관리진")
    }

    @Transactional(readOnly = true)
    fun checkAvailableSignUp(
        request: CheckAvailableRequest
    ): CheckAvailableResponse {
        val encryptedDeviceId = request.encryptedDeviceId
        val deviceId = decodeWithRsa.execute(encryptedDeviceId)

        val member = memberService.findMemberOp(deviceId)
        val suspended = suspendedService.findSuspensionByDeviceId(deviceId)
        val isBanned = suspended.map { it.isBanUser }.orElse(false)
        val withdrawn = suspended.map { !it.isBanUser }.orElse(false) && member.isEmpty
        val isRegistered = member.isPresent
        return CheckAvailableResponse(
            rejoinAvailableAt = suspended.map { it.untilBan }.orElse(null),
            banned = isBanned,
            withdrawn = withdrawn,
            registered = isRegistered,
        )
    }

    @Transactional(readOnly = true)
    fun validateNickname(nickname: String) : NicknameValidateResponse {
        return NicknameValidateResponse(
            nickname.isNotBlank() && !BadWordFilter.isBadWord(nickname) && nickname !in FORBIDDEN_NICKNAME
        )
    }

    @Transactional
    fun updateNickname(nickname: String, id: Long) {
        memberService.findMember(id).updateNickname(nickname)
    }

    @Transactional
    fun updateProfileImage(name: String?, id: Long) {
        val member = memberService.findMember(id)
        // 프로필 이미지가 null이거나 빈 문자열이면 함수 종료
        name?.takeIf { it.isNotBlank() }?.let { imgName ->
            // 이미지 저장 여부 확인
            if (!s3ImgService.isImgSaved(s3ImgPathProperties.profileImg, imgName)) {
                throw NoSuchElementException("Image not found in S3")
            }
            // 이미지 검토 중인지 확인
            if (rekognitionService.isModeratingImg(s3ImgPathProperties.profileImg, imgName)) {
                throw IllegalStateException("Image is under moderation")
            }

            // 프로필 이미지 업데이트 처리
            val profileImg = profileImgService.findProfileImg(imgName)
            profileImg.updateProfileOwner(member)

            // 멤버 프로필 이미지 이름 업데이트
            member.updateProfileImgName(imgName)
        }
    }



}