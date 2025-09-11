package com.clip.api.rsa.service

import com.clip.data.rsa.service.RsaService
import com.clip.global.redis.RedisUtils
import org.springframework.stereotype.Service

@Service
class RsaUseCase(
    private val rsaService: RsaService,
    private val redisUtils: RedisUtils,
) {

    fun getPublicKey(): String =
        runCatching {
            redisUtils.get("rsa:public-key:new")
                ?: rsaService.findLatestRsaKey().publicKey
        }.getOrElse {
            rsaService.findLatestRsaKey().publicKey
        }
}
