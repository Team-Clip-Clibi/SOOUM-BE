package com.clip.api.member.controller

import com.clip.api.docs.member.AuthDocs
import com.clip.api.member.controller.dto.*
import com.clip.api.member.service.AuthUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authUseCase: AuthUseCase
): AuthDocs {

    @DeleteMapping("/withdrawal")
    override fun withdrawal(
        @RequestBody withdrawalRequest: WithdrawalRequest,
        @AccessUser userId: Long
    ): ResponseEntity<Void> =
        authUseCase.withdrawal(withdrawalRequest, userId)
            .let { ResponseEntity.ok().build() }

    @PostMapping("/login")
    override fun login(
        @RequestBody loginRequest: LoginRequest,
        @RequestHeader("version", required = false) appVersion: String?
    ): ResponseEntity<LoginResponse> {
        val response = authUseCase.login(loginRequest, appVersion)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/sign-up")
    override fun signUp(
        @RequestBody signUpRequest: SignUpRequest,
        @RequestHeader("version", required = false) appVersion: String?
    ): ResponseEntity<SignUpResponse> {
        val response = authUseCase.signUp(signUpRequest, appVersion)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/token/reissue")
    override fun getReissueAccessToken(
        @RequestBody tokenDto: TokenDto,
        @RequestHeader("version", required = false) appVersion: String?,
    ): ResponseEntity<TokenDto> =
        authUseCase.reissueAccessToken(tokenDto, appVersion)
            .let { ResponseEntity.ok(it) }
}
