package com.clip.global

import com.clip.infra.rekognition.RekognitionConfig
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3Config
import com.clip.infra.s3.S3ImgPathProperties
import com.clip.infra.s3.S3ImgService
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest
class IntegrationTestSupport {
    @MockitoBean
    lateinit var s3Config: S3Config
    @MockitoBean
    lateinit var s3ImgService: S3ImgService
    @MockitoBean
    lateinit var s3ImgPathProperties: S3ImgPathProperties
    @MockitoBean
    lateinit var rekognitionConfig: RekognitionConfig
    @MockitoBean
    lateinit var rekognitionService: RekognitionService
}