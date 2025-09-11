package com.clip.api.docs.member

import com.clip.api.member.dto.LoginRequest
import com.clip.api.member.dto.LoginResponse
import com.clip.api.member.dto.SignUpRequest
import com.clip.api.member.dto.SignUpResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Auth", description = "Authentication API")
interface AuthDocs {

    @Operation(summary = "로그인 API", description = """
        - 사용자가 로그인할 수 있도록 합니다. 가입 가능 여부 확인 API에서 가입이 되어있는 사용자로 확인된 경우에만 로그인이 가능합니다.
        - 가입이 되어있는 사용자 : 가입 가능 여부 확인 API에서 banned, withdrawn, registered 값이 모두 false인 사용자
        - accessToken과 refreshToken을 발급합니다.
    """)
    fun login(loginRequest: LoginRequest) : ResponseEntity<LoginResponse>

    @Operation(summary = "회원가입 API", description = """
        - 사용자가 회원가입할 수 있도록 합니다. 가입 가능 여부 확인 API에서 가입이 가능한 사용자로 확인된 경우에만 회원가입이 가능합니다.
        - 가입이 가능한 사용자 : 가입 가능 여부 확인 API에서 banned, withdrawn 값이 false이고 registered 값이 true인 사용자
        - 회원가입 시 deviceId는 RSA로 암호화되어 전송됩니다.
    """)
    fun signUp(signUpRequest: SignUpRequest): ResponseEntity<SignUpResponse>
}