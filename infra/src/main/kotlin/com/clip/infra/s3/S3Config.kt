package com.clip.infra.s3

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config {
    @Value("\${spring.cloud.aws.rekognition.credentials.accessKey}")
    private lateinit var accessKey: String

    @Value("\${spring.cloud.aws.rekognition.credentials.secretKey}")
    private lateinit var secretKey: String

    @Value("\${spring.cloud.aws.region}")
    private lateinit var region: String

    @Bean
    fun s3Client(): S3Client{
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner{
        return S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .region(Region.of(region))
            .build()
    }

}