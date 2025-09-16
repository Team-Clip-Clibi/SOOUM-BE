package com.clip.api.member.service

import com.clip.api.member.dto.LoginRequest
import com.clip.api.member.dto.LoginResponse
import com.clip.api.member.dto.SignUpRequest
import com.clip.api.member.dto.SignUpResponse
import com.clip.api.member.dto.TokenDto
import com.clip.data.member.entity.Member
import com.clip.data.member.entity.PolicyTerm
import com.clip.data.member.service.MemberService
import com.clip.data.member.service.PolicyService
import com.clip.data.member.service.RefreshTokenService
import com.clip.global.security.jwt.JwtProvider
import com.clip.global.util.NicknameGenerator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthUseCase(
    private val decodeWithRsa: DecodeWithRsa,
    private val memberService: MemberService,
    private val refreshTokenService: RefreshTokenService,
    private val policyService: PolicyService,
    private val jwtProvider: JwtProvider,
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
                    .nickname(NicknameGenerator.generate())
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
            nickname = member.nickname,
        )
    }

    fun reissueAccessToken(request: TokenDto, userId: Long): TokenDto {
        val reissueToken = jwtProvider.reissueToken(request.refreshToken, userId)
        val refreshToken = refreshTokenService.findByMember(userId)
        refreshToken.update(reissueToken.refreshToken)
        refreshTokenService.save(refreshToken)

        return TokenDto(
            accessToken = reissueToken.accessToken,
            refreshToken = reissueToken.refreshToken
        )
    }

}