package com.clip.api.docs.member

import com.clip.api.member.controller.dto.LoginRequest
import com.clip.api.member.controller.dto.LoginResponse
import com.clip.api.member.controller.dto.SignUpRequest
import com.clip.api.member.controller.dto.SignUpResponse
import com.clip.api.member.controller.dto.TokenDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Auth", description = "Authentication API")
interface AuthDocs {

    @Operation(summary = "탈퇴 API", description = """
        - 사용자가 탈퇴할 수 있도록 합니다. 
        - 탈퇴 요청이 성공하면 200을 반환하며, 사용자의 Refresh Token, Access Token은 모두 사용불가처리 됩니다.
    """)
    fun withdrawal(withdrawalRequest: TokenDto, userId: Long): ResponseEntity<Void>

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

    @Operation(summary = "Access token 재발행 API", description = """
        - Access Token reissue API. Refresh Token을 사용해 새로운 Access Token을 발행합니다.
        - Refresh Token도 사용되었기 때문에 동일한 기간만큼만 유요한 새로운 Refresh Token도 함께 발행됩니다.
        - 기존의 Refresh Token은 폐기됩니다.
    """)
    fun getReissueAccessToken(tokenDto: TokenDto, userId: Long): ResponseEntity<TokenDto>
}