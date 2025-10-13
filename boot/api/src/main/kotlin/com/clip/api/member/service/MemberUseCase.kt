package com.clip.api.member.service

import com.clip.api.member.controller.dto.*
import com.clip.data.card.service.FeedCardService
import com.clip.data.follow.service.FollowService
import com.clip.data.img.service.ProfileImgService
import com.clip.data.member.entity.Member
import com.clip.data.member.service.MemberService
import com.clip.data.member.service.SuspendedService
import com.clip.data.visitor.service.VisitorService
import com.clip.global.exception.ImageException
import com.clip.global.util.BadWordFilter
import com.clip.global.util.NicknameGenerator
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3ImgPathProperties
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class MemberUseCase(
    private val suspendedService: SuspendedService,
    private val memberService: MemberService,
    private val decodeWithRsa: DecodeWithRsa,
    private val s3ImgService: S3ImgService,
    private val s3ImgPathProperties: S3ImgPathProperties,
    private val rekognitionService: RekognitionService,
    private val profileImgService: ProfileImgService,
    private val visitorService: VisitorService,
    private val feedCardService: FeedCardService,
    private val followService: FollowService,
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

    fun validateNickname(nicknameDto: NicknameDto) : NicknameValidateResponse {
        return NicknameValidateResponse(
            nicknameDto.nickname.isNotBlank() && !BadWordFilter.isBadWord(nicknameDto.nickname) && nicknameDto.nickname !in FORBIDDEN_NICKNAME
        )
    }

    @Transactional
    fun updateNickname(nicknameDto: NicknameDto, id: Long) {
        memberService.findMember(id).updateNickname(nicknameDto.nickname)
    }

    @Transactional
    fun updateProfileImage(profileImageDto: ProfileImageDto, id: Long) {
        val member = memberService.findMember(id)
        // 프로필 이미지가 null이거나 빈 문자열이면 함수 종료
        profileImageDto.imageName?.takeIf { it.isNotBlank() }?.let { imgName ->
            // 이미지 저장 여부 확인
            if (!s3ImgService.isImgSaved(s3ImgPathProperties.profileImg, imgName)) {
                throw ImageException.ImageNotFoundException(imgName = imgName)
            }
            // 이미지 검토 중인지 확인
            if (rekognitionService.isModeratingImg(s3ImgPathProperties.profileImg, imgName)) {
                throw ImageException.InvalidImageException(imgName = imgName)
            }

            // 프로필 이미지 업데이트 처리
            val profileImg = profileImgService.findProfileImg(imgName)
            profileImg.updateProfileOwner(member)

            // 멤버 프로필 이미지 이름 업데이트
            member.updateProfileImgName(imgName)
        }
    }

    @Transactional
    fun updateFCMToken(fcmToken: String, userId: Long) {
        val member = memberService.findMember(userId)
        member.updateFCMToken(fcmToken)
        memberService.save(member)
    }

    fun generateNickname() : NicknameDto =
        NicknameDto(NicknameGenerator.generate())

    @Transactional
    fun getPostingPermissions(userId: Long): PostingPermissionDto {
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val member = memberService.findMember(userId)

        val untilBan = member.untilBan

        if (untilBan != null && untilBan.isAfter(now))
            return PostingPermissionDto(true, member.untilBan)

        if (untilBan != null && untilBan.isBefore(now))
            member.unban()
            memberService.save(member)

        return PostingPermissionDto(false, member.untilBan)
    }

    @Transactional(readOnly = true)
    fun getMyProfileSummaryInfo(userId: Long): MyProfileInfoResponse {
        val member = memberService.findMember(userId)

        val totalVisitCnt = memberService.findTotalVisitCnt(member)
        val todayVisitCnt = visitorService.findCurrentDateVisitorCnt(member)
        val feedCardCnt = feedCardService.findFeedCardCnt(member)
        val followerCnt = followService.findFollowerCnt(member)
        val followingCnt = followService.findFollowingCnt(member)

        return MyProfileInfoResponse(
            member.pk,
            member.nickname,
            member.profileImgName?.let { s3ImgService.generateProfileImgUrl(it) },
            totalVisitCnt,
            todayVisitCnt,
            feedCardCnt,
            followingCnt,
            followerCnt
        )
    }

    @Transactional(readOnly = true)
    fun getUserProfileSummaryInfo(profileOwnerId: Long, userId: Long): UserProfileInfoResponse {
        val profileOwner = memberService.findMember(profileOwnerId)
        val visitor = memberService.findMember(userId)
        saveVisitor(visitor, profileOwner)

        val totalVisitCnt = memberService.findTotalVisitCnt(profileOwner)
        val todayVisitCnt = visitorService.findCurrentDateVisitorCnt(profileOwner)
        val feedCardCnt = feedCardService.findFeedCardCnt(profileOwner)
        val followerCnt = followService.findFollowerCnt(profileOwner)
        val followingCnt = followService.findFollowingCnt(profileOwner)
        val alreadyFollowing = followService.isAlreadyFollowing(visitor, profileOwner)

        return UserProfileInfoResponse(
            profileOwner.pk,
            profileOwner.nickname,
            profileOwner.profileImgName?.let { s3ImgService.generateProfileImgUrl(it) },
            totalVisitCnt,
            todayVisitCnt,
            feedCardCnt,
            followingCnt,
            followerCnt,
            alreadyFollowing
        )
    }

    private fun saveVisitor(visitor: Member, profileOwner: Member) {
        val isFirstVisitToday = visitorService.saveVisitorIfFirstVisitToday(profileOwner, visitor).isPresent
        if (isFirstVisitToday) {
            memberService.incrementTotalVisitorCnt(profileOwner)
        }
    }

}