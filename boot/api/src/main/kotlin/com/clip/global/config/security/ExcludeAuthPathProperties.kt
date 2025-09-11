package com.clip.global.config.security

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("exclude-auth-path-patterns")
data class ExcludeAuthPathProperties(
    val paths: List<AuthPath>
) {
    fun getExcludeAuthPaths(): List<String> {
        return paths.map { it.pathPattern }
    }

    data class AuthPath(
        val pathPattern: String,
        val method: String
    )
}