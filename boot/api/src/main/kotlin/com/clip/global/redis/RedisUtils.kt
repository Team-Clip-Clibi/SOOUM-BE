package com.clip.global.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

//@Component
//class RedisUtils(
//    private val stringRedisTemplate: StringRedisTemplate
//) {
//    fun get(key: String): String? = stringRedisTemplate.opsForValue().get(key)
//    fun set(key: String, value: String) = stringRedisTemplate.opsForValue().set(key, value)
//    fun setWithDuration(key: String, value: String, duration: Duration) =
//        stringRedisTemplate.opsForValue().set(key, value, duration)
//}
