package com.clip.api.member.service

import com.clip.api.member.controller.dto.LoginRequest
import com.clip.api.member.controller.dto.LoginResponse
import com.clip.api.member.controller.dto.SignUpRequest
import com.clip.api.member.controller.dto.SignUpResponse
import com.clip.api.member.controller.dto.TokenDto
import com.clip.data.img.service.ProfileImgService
import com.clip.data.member.entity.Blacklist
import com.clip.data.member.entity.Member
import com.clip.data.member.entity.PolicyTerm
import com.clip.data.member.service.BlacklistService
import com.clip.data.member.service.MemberService
import com.clip.data.member.service.PolicyService
import com.clip.data.member.service.RefreshTokenService
import com.clip.data.notification.service.NotificationHistoryService
import com.clip.global.exception.ImageException
import com.clip.global.exception.TokenException
import com.clip.global.security.jwt.JwtProvider
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3ImgPathProperties
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthUseCase(
    private val decodeWithRsa: DecodeWithRsa,
    private val memberService: MemberService,
    private val refreshTokenService: RefreshTokenService,
    private val blacklistService: BlacklistService,
    private val policyService: PolicyService,
    private val jwtProvider: JwtProvider,
    private val s3ImgService: S3ImgService,
    private val rekognitionService: RekognitionService,
    private val s3ImgPathProperties: S3ImgPathProperties,
    private val profileImgService: ProfileImgService,
    private val notificationHistoryService: NotificationHistoryService,
) {

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val encryptedDeviceId = request.encryptedDeviceId
        val deviceId = decodeWithRsa.execute(encryptedDeviceId)

        val member = memberService.findByDeviceId(deviceId)
        val accessToken = jwtProvider.createAccessToken(member.pk, member.role)
        val refreshToken = jwtProvider.createRefreshToken(member.pk, member.role)

        refreshTokenService.findByMember(member.pk).update(refreshToken)

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun signUp(request: SignUpRequest): SignUpResponse {
        val encryptedDeviceId = request.memberInfo.encryptedDeviceId
        val deviceId = decodeWithRsa.execute(encryptedDeviceId)
        request.memberInfo.profileImage?.takeIf { it.isNotBlank() }?.let { imgName ->
            // 이미지 저장 여부 확인
            if (!s3ImgService.isImgSaved(s3ImgPathProperties.profileImg, imgName)) {
                throw ImageException.ImageNotFoundException(imgName = imgName)
            }
            // 이미지 검토 (부적절한 이미지 여부)
            if (rekognitionService.isModeratingImg(s3ImgPathProperties.profileImg, imgName)) {
                throw ImageException.InvalidImageException(imgName = imgName)
            }
        }

        val findMemberOp = memberService.findMemberOp(deviceId)
        val member = if (findMemberOp.isPresent) {
            val existingMember = findMemberOp.get()
            refreshTokenService.deleteRefreshToken(existingMember.pk)
            existingMember
        } else {
            val newMember = memberService.save(
                Member.builder()
                    .deviceId(deviceId)
                    .deviceType(request.memberInfo.deviceType)
                    .firebaseToken(request.memberInfo.fcmToken)
                    .nickname(request.memberInfo.nickname)
                    .profileImgName(request.memberInfo.profileImage)
                    .isAllowNotify(true)
                    .build()
            )
            policyService.save(
                PolicyTerm.builder()
                    .member(newMember)
                    .isAllowTermOne(request.policy.agreedToTermsOfService)
                    .isAllowTermTwo(request.policy.agreedToLocationTerms)
                    .isAllowTermThree(request.policy.agreedToPrivacyPolicy)
                    .build()
            )
            newMember
        }

        val accessToken = jwtProvider.createAccessToken(member.pk, member.role)
        val refreshToken = jwtProvider.createRefreshToken(member.pk, member.role)

        refreshTokenService.save(refreshToken, member)

        return SignUpResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun reissueAccessToken(request: TokenDto): TokenDto {
        if (blacklistService.findByToken(request.refreshToken).isPresent)
            throw TokenException.BlacklistTokenException(token = request.refreshToken)

        val userId = runCatching { jwtProvider.getUserId(request.refreshToken) }
            .getOrElse { throw TokenException.ExpiredTokenException(token = request.refreshToken) }

        val reissueToken = jwtProvider.reissueToken(request.refreshToken, userId)
        val refreshToken = refreshTokenService.findByMember(userId)
            .update(reissueToken.refreshToken)
        refreshTokenService.save(refreshToken)

        blacklistService.save(
            Blacklist(
                request.accessToken,
                jwtProvider.getTokenExpiration(request.accessToken)
            )
        )

        blacklistService.save(
            Blacklist(
                request.refreshToken,
                jwtProvider.getTokenExpiration(request.refreshToken)
            )
        )

        return TokenDto(
            accessToken = reissueToken.accessToken,
            refreshToken = reissueToken.refreshToken
        )
    }

    @Transactional
    fun withdrawal(withdrawalRequest: TokenDto, userId: Long) {
        blacklistService.save(
            Blacklist(
                withdrawalRequest.accessToken,
                jwtProvider.getTokenExpiration(withdrawalRequest.accessToken)
            )
        )
        blacklistService.save(
            Blacklist(
                withdrawalRequest.refreshToken,
                jwtProvider.getTokenExpiration(withdrawalRequest.refreshToken)
            )
        )
        profileImgService.updateProfileImgNull(userId)
        policyService.deletePolicyTerm(userId)
        refreshTokenService.deleteRefreshToken(userId)
        notificationHistoryService.deleteAllNotificationHistory(userId)
        memberService.deleteMember(userId)
    }
}