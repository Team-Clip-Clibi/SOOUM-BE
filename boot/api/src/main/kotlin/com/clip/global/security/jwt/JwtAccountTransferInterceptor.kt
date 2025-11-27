package com.clip.global.security.jwt

import com.clip.data.member.service.AccountTransferHistoryService
import com.clip.global.exception.TokenException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtAccountTransferInterceptor(
    private val jwtProvider: JwtProvider,
    private val accountTransferHistoryService: AccountTransferHistoryService
) : HandlerInterceptor {

    companion object {
        const val BEARER_PREFIX = "Bearer "
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.removePrefix(BEARER_PREFIX)
            ?: throw TokenException.InvalidTokenException("Authorization 헤더에 Bearer 토큰이 존재하지 않습니다.")
        val userId = jwtProvider.getUserId(token)
        val deviceId = jwtProvider.getDeviceId(token)
        checkIsAccountAlreadyTransferred(userId, deviceId)
        return true
    }

    private fun checkIsAccountAlreadyTransferred(
        userId: Long,
        deviceId: String
    ) {
        accountTransferHistoryService.findByMemberPk(userId).ifPresent {  history ->
            if (history.member.deviceId != deviceId) {
                throw TokenException.AlreadyAccountTransferredException(
                    "이미 계정 이전이 완료된 사용자입니다. userId: $userId",
                    userId
                )
            }
        }
    }
}