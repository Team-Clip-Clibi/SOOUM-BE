package com.clip.global.security.resolver

import com.clip.global.security.annotation.AccessUser
import com.clip.global.security.jwt.JwtProvider
import org.springframework.core.MethodParameter
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AccessUserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasAnnotation = parameter.hasParameterAnnotation(AccessUser::class.java)
        val parameterType = Long::class.java.isAssignableFrom(parameter.parameterType)
        return hasAnnotation && parameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication is AnonymousAuthenticationToken) {
            return 0L
        }

        return when (val principal = authentication.principal) {
            is User -> principal.username.toLongOrNull()
            is String -> principal.toLongOrNull()
            else -> null
        } ?: 0L
    }

}
