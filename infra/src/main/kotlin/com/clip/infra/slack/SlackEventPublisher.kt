package com.clip.infra.slack

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.WebUtils
import java.time.LocalDateTime

@Component
class SlackEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    /**
     * Slack 알림 이벤트 발행
     * - HttpServletRequest는 ThreadLocal(RequestContextHolder)에서 자동 추출
     *
     * @param message 메시지 제목/내용 (선택사항)
     * @param exception 예외 (선택사항 - 에러 알림일 경우)
     * @param userId 사용자 ID (선택사항)
     * @param customFields 추가 정보 (비즈니스 컨텍스트)
     * @param includeRequestInfo Request 정보 포함 여부 (기본: true, 배치/스케줄러는 false)
     */
    fun publish(
        message: String? = null,
        exception: Exception? = null,
        userId: Long? = null,
        customFields: Map<String, Any?> = emptyMap(),
        includeRequestInfo: Boolean = true
    ) {
        val requestInfo = if (includeRequestInfo) {
            extractRequestInfoFromContext()
        } else null

        applicationEventPublisher.publishEvent(
            SlackNotificationEvent(
                message = message,
                exception = exception,
                requestInfo = requestInfo,
                userId = userId,
                customFields = customFields
            )
        )
    }

    private fun extractRequestInfoFromContext(): RequestInfo? {
        return try {
            val attributes = RequestContextHolder.getRequestAttributes()
                    as? ServletRequestAttributes
            attributes?.request?.let { extractRequestInfo(it) }
        } catch (e: Exception) {
            null
        }
    }

    private fun extractRequestInfo(request: HttpServletRequest): RequestInfo {
        return RequestInfo(
            url = request.requestURL.toString(),
            method = request.method,
            pathVariables = extractPathVariables(request),
            params = request.parameterMap.mapValues { it.value.joinToString() },
            body = extractBody(request),
            remoteAddr = getClientIp(request)
        )
    }

    private fun getClientIp(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
            ?.split(",")
            ?.firstOrNull()
            ?.trim()
            ?.takeUnless { it.isEmpty() || it.equals("unknown", ignoreCase = true) }

        return xForwardedFor ?: request.remoteAddr
    }

    private fun extractBody(request: HttpServletRequest): String? {
        val cachingRequest = WebUtils.getNativeRequest(
            request,
            ContentCachingRequestWrapper::class.java
        ) ?: return null

        val content = cachingRequest.contentAsByteArray
        return if (content.isNotEmpty()) {
            String(content, Charsets.UTF_8)
        } else null
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractPathVariables(request: HttpServletRequest): Map<String, String> {
        return try {
            val pathVariables = request.getAttribute(
                "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables"
            ) as? Map<String, String>

            pathVariables ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }
}

data class SlackNotificationEvent(
    val message: String?,
    val exception: Exception?,
    val requestInfo: RequestInfo?,
    val userId: Long?,
    val customFields: Map<String, Any?>,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

data class RequestInfo(
    val url: String,
    val method: String,
    val pathVariables: Map<String, String>,
    val params: Map<String, String>,
    val body: String?,
    val remoteAddr: String
)