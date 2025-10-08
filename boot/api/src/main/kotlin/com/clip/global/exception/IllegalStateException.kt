package com.clip.global.exception

import org.springframework.http.HttpStatus

sealed class IllegalStateException(
    httpStatus: HttpStatus,
    message: String = "처리할 수 없는 상태입니다.",
): BaseException(httpStatus, message){

    class IllegalStatementException(
        message: String = "처리할 수 없는 상태입니다.",
        vararg parameters: Any?
    ): IllegalStateException(
        HttpStatus.BAD_REQUEST,
        message,
    )
}