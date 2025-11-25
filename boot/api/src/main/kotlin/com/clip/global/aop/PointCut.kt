package com.clip.global.aop

import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Component
class PointCut {
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    fun requesterPointcut() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    fun exceptionHandlerPointcut() {}
}