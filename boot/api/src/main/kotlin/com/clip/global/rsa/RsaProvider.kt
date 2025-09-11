package com.clip.global.rsa

import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

@Component
class RsaProvider {

    fun decode(encryptedData: String, oldPrivateKey: String, newPrivateKey: String): String {
        return runCatching {
            decryptData(newPrivateKey, encryptedData)
        }.recoverCatching {
            decryptData(oldPrivateKey, encryptedData)
        }.getOrElse {
            throw RuntimeException("RSA 복호화 중 오류가 발생했습니다." + it.message)
        }
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun decryptData(privateKey: String, encryptedData: String): String {
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
        val privateKeyObj = keyFactory.generatePrivate(keySpec)

        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKeyObj)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData))
        return String(decryptedBytes)
    }
}