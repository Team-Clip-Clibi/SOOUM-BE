package com.clip.api.member.controller

import com.clip.api.docs.member.AuthDocs
import com.clip.api.member.controller.dto.LoginRequest
import com.clip.api.member.controller.dto.LoginResponse
import com.clip.api.member.controller.dto.SignUpRequest
import com.clip.api.member.controller.dto.SignUpResponse
import com.clip.api.member.controller.dto.TokenDto
import com.clip.api.member.service.AuthUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authUseCase: AuthUseCase
): AuthDocs {

    @DeleteMapping("/withdrawal")
    override fun withdrawal(
        @RequestBody withdrawalRequest: TokenDto,
        @AccessUser userId: Long
    ): ResponseEntity<Void> =
        authUseCase.withdrawal(withdrawalRequest, userId)
            .let { ResponseEntity.ok().build() }

    @PostMapping("/login")
    override fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authUseCase.login(loginRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/sign-up")
    override fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<SignUpResponse> {
        val response = authUseCase.signUp(signUpRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/token/reissue")
    override fun getReissueAccessToken(
        @RequestBody tokenDto: TokenDto,
    ): ResponseEntity<TokenDto> =
        authUseCase.reissueAccessToken(tokenDto)
            .let { ResponseEntity.ok(it) }
}