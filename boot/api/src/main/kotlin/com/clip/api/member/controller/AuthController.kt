package com.clip.api.member.controller

import com.clip.api.docs.member.AuthDocs
import com.clip.api.member.dto.LoginRequest
import com.clip.api.member.dto.LoginResponse
import com.clip.api.member.dto.SignUpRequest
import com.clip.api.member.dto.SignUpResponse
import com.clip.api.member.service.AuthUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authUseCase: AuthUseCase
): AuthDocs {

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


}