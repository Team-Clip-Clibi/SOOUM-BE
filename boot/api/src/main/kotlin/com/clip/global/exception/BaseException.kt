package com.clip.global.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus

private val log = KotlinLogging.logger {}

abstract class BaseException(
    val httpStatus: HttpStatus = DEFAULT_HTTP_STATUS,
    override val message: String = DEFAULT_ERROR_MESSAGE,
    vararg val parameters: Any?
): RuntimeException() {

    init {
        log.error { "message: $message, params=${parameters.toList()}" }
    }

    companion object{
        const val DEFAULT_ERROR_MESSAGE = "BaseException"
        val DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR
    }
}