package com.clip.global.aop

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class RequesterInfoAspect {

    private val log = KotlinLogging.logger {}

    @Before("com.clip.global.aop.PointCut.requesterPointcut()")
    fun before() {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).getRequest()

        var ip = request.getHeader("X-Forwarded-For")
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getRemoteAddr()
        }

        log.info {
            "[Request URI]: ${request.requestURI}, " +
                    "[Auth Header]: ${request.getHeader("Authorization")}, " +
                    "[User-Agent]: ${request.getHeader("User-Agent")}," +
                    " [Client IP]: $ip"
        }
    }
}