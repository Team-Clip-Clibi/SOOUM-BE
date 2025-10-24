package com.clip.infra.s3

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.io.InputStream

@Service
class S3FcmService(
    private val s3FcmProperties: S3FcmProperties,
    private val s3Client: S3Client,
) {

    fun findFcmSdk(): InputStream =
        s3Client.getObject(
            GetObjectRequest.builder()
                .bucket(s3FcmProperties.bucket)
                .key(s3FcmProperties.sdk)
                .build()
        )
}