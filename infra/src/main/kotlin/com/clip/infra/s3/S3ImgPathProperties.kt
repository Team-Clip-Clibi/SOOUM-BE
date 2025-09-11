package com.clip.infra.s3

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
class S3ImgPathProperties (
    val defaultCardImg: String,
    val userCardImg: String,
    val profileImg: String
)