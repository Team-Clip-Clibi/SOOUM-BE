package com.clip.api.img.service

import com.clip.global.util.YamlPropertySourceFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ConfigurationProperties(prefix = "default-img")
@PropertySource("classpath:img/DefaultImageName.yml", factory = YamlPropertySourceFactory::class)
class DefaultImageProperties {
    lateinit var readOnly: List<String>
    lateinit var color: List<String>
    lateinit var nature: List<String>
    lateinit var daily: List<String>
    lateinit var food: List<String>
}