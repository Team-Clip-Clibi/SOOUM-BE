package com.clip.global.config.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration
import java.time.LocalDateTime

//@EnableCaching
//@Configuration
//@EnableRedisRepositories
//class RedisConfig {
//
//    @Value("\${spring.data.redis.host}")
//    private lateinit var host: String
//
//    @Value("\${spring.data.redis.port}")
//    private var port: Int = 0
//
//    @Bean
//    fun redisConnectionFactory(): LettuceConnectionFactory {
//        val redisStandaloneConfiguration = RedisStandaloneConfiguration(host, port)
//        return LettuceConnectionFactory(redisStandaloneConfiguration)
//    }
//
//    @Bean
//    fun stringLocalDateTimeRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, LocalDateTime> {
//        val redisTemplate = RedisTemplate<String, LocalDateTime>()
//        redisTemplate.connectionFactory = redisConnectionFactory
//        redisTemplate.keySerializer = StringRedisSerializer()
//        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
//        redisTemplate.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
//        return redisTemplate
//    }
//
//    @Bean
//    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
//        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
//            .entryTtl(Duration.ofSeconds(5))
//
//        val cacheConfiguration = mapOf(
//            "unreadNotificationCnt" to redisCacheConfiguration,
//            "unreadCardNotificationCnt" to redisCacheConfiguration,
//            "unreadLikeNotificationCnt" to redisCacheConfiguration
//        )
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//            .withInitialCacheConfigurations(cacheConfiguration)
//            .build()
//    }
//}