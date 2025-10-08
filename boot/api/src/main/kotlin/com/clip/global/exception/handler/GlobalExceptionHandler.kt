package com.clip.global.exception.handler

import com.clip.global.exception.BaseException
import com.clip.global.exception.IllegalArgumentException
import com.clip.global.exception.ImageException
import com.clip.global.exception.TokenException
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.message ?: "Entity not found")
        return ResponseEntity.status(404).body(errorResponse)
    }

    @ExceptionHandler(TokenException.InvalidTokenException::class)
    fun invalidTokenException(e: TokenException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, e.httpStatus, e.parameters.contentToString() + e.message)
        return ResponseEntity.status(e.httpStatus).body(errorResponse)
    }

    @ExceptionHandler(TokenException.ExpiredTokenException::class)
    fun expiredTokenException(e: TokenException.ExpiredTokenException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, e.httpStatus, e.parameters.contentToString() + e.message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    @ExceptionHandler(ImageException.InvalidImageException::class)
    fun invalidImageException(e: ImageException.InvalidImageException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, e.httpStatus, e.parameters.contentToString() + e.message)
        return ResponseEntity.status(e.httpStatus).body(errorResponse)
    }

    @ExceptionHandler(ImageException.ImageNotFoundException::class)
    fun imageNotFoundException(e: ImageException.ImageNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, e.httpStatus, e.parameters.contentToString() + e.message)
        return ResponseEntity.status(e.httpStatus).body(errorResponse)
    }

    @ExceptionHandler(IllegalArgumentException.ParameterNotFoundException::class)
    fun parameterNotFoundException(e: IllegalArgumentException.ParameterNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, e.httpStatus, e.parameters.contentToString() + e.message)
        return ResponseEntity.status(e.httpStatus).body(errorResponse)
    }


    @ExceptionHandler(BaseException::class)
    fun baseException(e: BaseException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, e.httpStatus, e.parameters.contentToString() + e.message)
        return ResponseEntity.status(e.httpStatus).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun exception(e: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, e.message ?: "Internal server error")
        return ResponseEntity.status(500).body(errorResponse)
    }
}