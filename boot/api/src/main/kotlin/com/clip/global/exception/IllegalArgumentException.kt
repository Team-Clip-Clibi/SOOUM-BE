package com.clip.global.exception

import org.springframework.http.HttpStatus

sealed class IllegalArgumentException(
    httpStatus: HttpStatus,
    message: String = "이미지 처리 중 오류가 발생했습니다.",
): BaseException(httpStatus, message) {

    class ParameterNotFoundException(
        message: String = "요청 파라미터가 누락되었습니다.",
        vararg parameters: Any?
    ): IllegalArgumentException(
        HttpStatus.BAD_REQUEST,
        message,
    )
}