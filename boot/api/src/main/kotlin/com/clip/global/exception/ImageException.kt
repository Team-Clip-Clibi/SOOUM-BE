package com.clip.global.exception

import org.springframework.http.HttpStatus

sealed class ImageException (
    httpStatus: HttpStatus,
    message: String = "이미지 처리 중 오류가 발생했습니다.",
    vararg parameters: Any?
): BaseException(httpStatus, message, parameters) {

    class ImageNotFoundException(
        message: String = "이미지 파일이 존재하지 않습니다.",
        imgName: String? = null
    ) : ImageException(
        HttpStatus.NOT_FOUND,
        message,
        imgName
    )

    class InvalidImageException(
        message: String = "부적절한 이미지 파일입니다.",
        imgName: String? = null
    ) : TokenException(
        HttpStatus.UNPROCESSABLE_ENTITY,
        message,
        imgName
    )
}