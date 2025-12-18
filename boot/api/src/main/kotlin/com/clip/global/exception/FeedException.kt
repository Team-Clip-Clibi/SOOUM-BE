package com.clip.global.exception

import org.springframework.http.HttpStatus

sealed class FeedException(
    httpStatus: HttpStatus,
    message: String
) : BaseException(httpStatus, message) {

    class FeedCardFetchLimitExceededException(
        message: String = "피드 카드를 충분히 구성하지 못했습니다."
    ) : FeedException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        message
    )
}