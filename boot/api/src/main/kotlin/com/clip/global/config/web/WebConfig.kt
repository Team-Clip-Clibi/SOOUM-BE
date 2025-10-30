package com.clip.global.config.web

import com.clip.global.config.security.ExcludeAuthPathProperties
import com.clip.global.security.jwt.JwtAccountTransferInterceptor
import com.clip.global.security.jwt.JwtBlacklistInterceptor
import com.clip.global.security.resolver.AccessUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val accessUserArgumentResolver: AccessUserArgumentResolver,
    private val jwtBlacklistInterceptor: JwtBlacklistInterceptor,
    private val jwtAccountTransferInterceptor: JwtAccountTransferInterceptor,
    private val excludeAuthPathProperties: ExcludeAuthPathProperties,
): WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(accessUserArgumentResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtBlacklistInterceptor)
            .excludePathPatterns(excludeAuthPathProperties.getExcludeAuthPaths())

        registry.addInterceptor(jwtAccountTransferInterceptor)
            .excludePathPatterns(excludeAuthPathProperties.getExcludeAuthPaths())
    }
}