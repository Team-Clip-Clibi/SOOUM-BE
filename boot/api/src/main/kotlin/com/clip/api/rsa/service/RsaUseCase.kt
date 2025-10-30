package com.clip.api.rsa.service

import com.clip.data.rsa.service.RsaService
import com.clip.global.redis.RedisUtils
import com.clip.global.rsa.RsaProvider
import org.springframework.stereotype.Service

@Service
class RsaUseCase(
    private val rsaService: RsaService,
    private val redisUtils: RedisUtils,
    private val rsaProvider: RsaProvider
) {

    fun getPublicKey(): String =
        runCatching {
            redisUtils.get("rsa:public-key:new")
                ?: rsaService.findLatestRsaKey().publicKey
        }.getOrElse {
            rsaService.findLatestRsaKey().publicKey
        }

    fun decodeDeviceId(encryptedData: String): String {
        val (newKey, oldKey) = runCatching {
            val new = redisUtils.get("rsa:private-key:new")
            val old = redisUtils.get("rsa:private-key:old")
            if (new != null) new to old else null
        }.getOrNull() ?: run {
            val rsa = rsaService.findLatestRsaKey()
            rsa.privateKey to rsa.privateKey
        }

        return rsaProvider.decode(encryptedData, oldKey ?: newKey, newKey)
    }

}
