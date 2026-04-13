package com.clip.global.exception

import org.springframework.http.HttpStatus


abstract class BaseException(
    val httpStatus: HttpStatus = DEFAULT_HTTP_STATUS,
    override val message: String = DEFAULT_ERROR_MESSAGE,
    vararg val parameters: Any?
): RuntimeException() {

    companion object{
        const val DEFAULT_ERROR_MESSAGE = "BaseException"
        val DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR
    }
}