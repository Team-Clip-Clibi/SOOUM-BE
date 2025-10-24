package com.clip.infra.s3

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cloud.aws.s3.fcm")
class S3FcmProperties (
    val bucket: String,
    val sdk: String,
)