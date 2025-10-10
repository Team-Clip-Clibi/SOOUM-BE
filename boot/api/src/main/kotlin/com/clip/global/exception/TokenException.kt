package com.clip.global.exception

import org.springframework.http.HttpStatus

sealed class TokenException(
    httpStatus: HttpStatus,
    message: String = "유효하지 않은 토큰입니다.",
    vararg parameters: Any?
): BaseException(httpStatus, message, parameters) {

    class InvalidTokenException(
        message: String = "유효하지 않은 토큰입니다.",
        token: String? = null
    ) : TokenException(
        HttpStatus.UNAUTHORIZED,
        message,
        token
    )

    class ExpiredTokenException(
        message: String = "만료된 토큰입니다.",
        token: String? = null
    ) : TokenException(
        HttpStatus.FORBIDDEN,
        message,
        token
    )

    class BlacklistTokenException(
        message: String = "블랙리스트에 등록된 토큰입니다.",
        token: String? = null
    ) : TokenException(
        HttpStatus.FORBIDDEN,
        message,
        token
    )
}