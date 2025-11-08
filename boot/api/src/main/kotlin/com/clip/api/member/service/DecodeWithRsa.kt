package com.clip.api.member.service

import com.clip.data.rsa.service.RsaService
import com.clip.global.rsa.RsaProvider
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DecodeWithRsa(
    private val rsaService: RsaService,
    private val rsaProvider: RsaProvider
) {
    @Transactional(readOnly = true)
    fun execute(encryptedData: String): String {
        val rsa = rsaService.findLatestRsaKey()
        val privateKeyNew = rsa.privateKey
        val privateKeyOld = rsa.privateKey

        return rsaProvider.decode(encryptedData, privateKeyOld, privateKeyNew)
    }
}
