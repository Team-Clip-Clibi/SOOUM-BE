package com.clip.api.member.service

import com.clip.data.rsa.service.RsaService
import com.clip.global.redis.RedisUtils
import com.clip.global.rsa.RsaProvider
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DecodeWithRsa(
    private val redisUtils: RedisUtils,
    private val rsaService: RsaService,
    private val rsaProvider: RsaProvider
) {
    @Transactional(readOnly = true)
    fun execute(encryptedData: String): String {
        val (privateKeyNew, privateKeyOld) = runCatching {
            val newKey = redisUtils.get("rsa:private-key:new")
            val oldKey = redisUtils.get("rsa:private-key:old")

            val privateKeyNew = newKey ?: rsaService.findLatestRsaKey().privateKey
            val privateKeyOld = oldKey ?: privateKeyNew

            Pair(privateKeyNew, privateKeyOld)
        }.getOrElse {
            val rsa = rsaService.findLatestRsaKey()
            Pair(rsa.privateKey, rsa.privateKey)
        }

        return rsaProvider.decode(encryptedData, privateKeyOld, privateKeyNew)
    }
}
