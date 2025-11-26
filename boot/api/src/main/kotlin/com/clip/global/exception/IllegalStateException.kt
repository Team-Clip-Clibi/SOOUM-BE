package com.clip.global.exception

import org.springframework.http.HttpStatus

sealed class IllegalStateException(
    httpStatus: HttpStatus,
    message: String = "처리할 수 없는 상태입니다.",
): BaseException(httpStatus, message){

//    class IllegalStatementException(
//        message: String = "처리할 수 없는 상태입니다.",
//        vararg parameters: Any?
//    ): IllegalStateException(
//        HttpStatus.BAD_REQUEST,
//        message,
//    )

    class CardWriteNotAllowedException(
        message: String = "글 작성 권한이 없습니다.",
        vararg parameters: Any?
    ): IllegalStateException(
        HttpStatus.BAD_REQUEST,
        message,
    )

    class AlreadyCompletedException(
        message: String = "이미 완료된 요청입니다.",
        vararg parameters: Any?
    ): IllegalStateException(
        HttpStatus.BAD_REQUEST,
        message,
    )

    class LimitOverException(
        message: String = "제한을 초과한 요청입니다.",
        vararg parameters: Any?
    ): IllegalStateException(
        HttpStatus.BAD_REQUEST,
        message,
    )

    class SuspendedUserException(
        message: String = "현재 회원가입을 할 수 없는 상태입니다.",
        vararg parameters: Any?
    ): IllegalStateException(
        HttpStatus.BAD_REQUEST,
        message,
    )
}