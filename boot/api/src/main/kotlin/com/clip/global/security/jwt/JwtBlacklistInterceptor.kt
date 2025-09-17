package com.clip.global.security.jwt

import com.clip.data.member.service.BlacklistService
import com.clip.global.exception.TokenException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtBlacklistInterceptor(
    private val blacklistService: BlacklistService
) : HandlerInterceptor {

    companion object {
        const val BEARER_PREFIX = "Bearer "
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.removePrefix(BEARER_PREFIX)
            ?.also {
                if (isBlackListToken(it)) {
                    throw TokenException.InvalidTokenException("블랙리스트에 등록된 토큰입니다.", it)
                }
            }
            ?: throw TokenException.InvalidTokenException("Authorization 헤더에 Bearer 토큰이 존재하지 않습니다.")


        return true
    }

    private fun isBlackListToken(token: String): Boolean {
        return blacklistService.findByToken(token).isPresent
    }
}