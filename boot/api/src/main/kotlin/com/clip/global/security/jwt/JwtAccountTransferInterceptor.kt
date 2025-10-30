package com.clip.global.security.jwt

import com.clip.data.member.service.AccountTransferHistoryService
import com.clip.global.exception.TokenException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.LocalDateTime

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
        val tokenIssuedAt = jwtProvider.getIssuedAt(token)
        val userId = jwtProvider.getUserId(token)
        checkIsAccountAlreadyTransferred(tokenIssuedAt, userId)
        return true
    }

    private fun checkIsAccountAlreadyTransferred(
        tokenIssuedAt: LocalDateTime,
        userId: Long
    ) {
        accountTransferHistoryService.findLatestByMemberPk(userId).ifPresent { history ->
            if (tokenIssuedAt.isBefore(history.transferAt)) {
                throw TokenException.AlreadyAccountTransferredException(
                    "이미 계정 이전이 완료된 사용자입니다.",
                    userId
                )
            }
        }
    }
}