package com.clip.global.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties(
    var key: String
) {

    companion object{
        const val ACCESS_TOKEN_EXPIRE_DAY: Long = 1 // 1일
        const val REFRESH_TOKEN_EXPIRE_MONTH: Long = 3 // 3개월
    }
}