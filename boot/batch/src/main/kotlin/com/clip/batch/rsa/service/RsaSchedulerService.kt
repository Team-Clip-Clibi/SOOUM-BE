package com.clip.batch.rsa.service

import com.clip.data.rsa.service.RsaService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
class RsaSchedulerService(
    private val stringRedisTemplate: StringRedisTemplate,
    private val rsaService: RsaService
) {
    companion object {
        const val KEY_SIZE = 1024
    }

    @Transactional
    @Throws(NoSuchAlgorithmException::class)
    fun save(currentDateTime: LocalDateTime) {
        val keyPair = generateKeyPair()

        // Save to RDB
        val newKeyExpiredAt = currentDateTime.plusDays(1).plusMinutes(10)
        val oldKeyExpiredAt = currentDateTime.plusMinutes(10)
        val rsa = rsaService.save(keyPair, newKeyExpiredAt)
        rsaService.deleteExpiredRsaKey()

        // Save to Redis
        val oldPublicKey = stringRedisTemplate.opsForValue().get("rsa:public-key:new")
        val oldPrivateKey = stringRedisTemplate.opsForValue().get("rsa:private-key:new")

        stringRedisTemplate.opsForValue().set(
            "rsa:public-key:new",
            rsa.publicKey,
            Duration.between(currentDateTime, newKeyExpiredAt)
        )

        stringRedisTemplate.opsForValue().set(
            "rsa:private-key:new",
            rsa.privateKey,
            Duration.between(currentDateTime, newKeyExpiredAt)
        )

        if (oldPublicKey != null || oldPrivateKey != null) {
            oldPublicKey?.let {
                stringRedisTemplate.opsForValue().set(
                    "rsa:public-key:old",
                    it,
                    Duration.between(currentDateTime, oldKeyExpiredAt)
                )
            }
            oldPrivateKey?.let {
                stringRedisTemplate.opsForValue().set(
                    "rsa:private-key:old",
                    it,
                    Duration.between(currentDateTime, oldKeyExpiredAt)
                )
            }
        }
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun generateKeyPair(): HashMap<String, String> {
        val secureRandom = SecureRandom()
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA").apply {
            initialize(KEY_SIZE, secureRandom)
        }

        val keyPair = keyPairGenerator.generateKeyPair()
        val stringPublicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)
        val stringPrivateKey = Base64.getEncoder().encodeToString(keyPair.private.encoded)

        return hashMapOf(
            "publicKey" to stringPublicKey,
            "privateKey" to stringPrivateKey
        )
    }
}
