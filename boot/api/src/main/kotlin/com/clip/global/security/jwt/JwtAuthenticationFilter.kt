package com.clip.global.security.jwt

import com.clip.global.config.security.ExcludeAuthPathProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.server.PathContainer
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.pattern.PathPatternParser

import java.io.IOException

class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val excludeAuthPathProperties: ExcludeAuthPathProperties
) : OncePerRequestFilter() {

    companion object {
        private val pathPatternParser = PathPatternParser()
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isExcludedPath(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = request.getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.substring(BEARER_PREFIX.length)
            ?: return unauthorizedResponse(response, "Missing or invalid Authorization")


        if (jwtProvider.validateToken(token)) {
            setAuthentication(token)
            filterChain.doFilter(request, response)
        } else {
            return unauthorizedResponse(response, "Invalid token")
        }
    }

    private fun isExcludedPath(request: HttpServletRequest): Boolean {
        val requestPath = request.requestURI
        val requestMethod = HttpMethod.valueOf(request.method)

        return excludeAuthPathProperties.paths.any { authPath ->
            pathPatternParser.parse(authPath.pathPattern)
                .matches(PathContainer.parsePath(requestPath)) &&
                    HttpMethod.valueOf(authPath.method) == requestMethod
        }
    }

    private fun setAuthentication(token: String) {
        jwtProvider.getAuthentication(token).takeIf { jwtProvider.validateToken(token) }?.let { authentication ->
            SecurityContextHolder.getContext().authentication = authentication
        }
    }

    private fun unauthorizedResponse(response: HttpServletResponse, message: String) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        response.writer.write("""{"code":"UNAUTHORIZED","message":"$message"}""")
    }
}