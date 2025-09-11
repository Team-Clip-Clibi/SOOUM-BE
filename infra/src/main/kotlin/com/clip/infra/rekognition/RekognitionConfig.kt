package com.clip.infra.rekognition

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

@Configuration
class RekognitionConfig {

    @Value("\${spring.cloud.aws.rekognition.credentials.accessKey}")
    private lateinit var accessKey: String

    @Value("\${spring.cloud.aws.rekognition.credentials.secretKey}")
    private lateinit var secretKey: String

    @Value("\${spring.cloud.aws.region}")
    private lateinit var region: String

    @Bean
    fun amazonRekognition(): RekognitionClient {
        return RekognitionClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build()
    }
}