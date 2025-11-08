package com.clip.api.rsa.service

import com.clip.data.rsa.service.RsaService
import com.clip.global.rsa.RsaProvider
import org.springframework.stereotype.Service

@Service
class RsaUseCase(
    private val rsaService: RsaService,
    private val rsaProvider: RsaProvider
) {

    fun getPublicKey(): String = rsaService.findLatestRsaKey().publicKey

    fun decodeDeviceId(encryptedData: String): String {
        val (newKey, oldKey) = rsaService.findLatestRsaKey().let { it.privateKey to it.privateKey }
        return rsaProvider.decode(encryptedData, oldKey ?: newKey, newKey)
    }

}
