package com.clip.infra.rekognition

import com.clip.infra.s3.S3ImgPathProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsRequest
import software.amazon.awssdk.services.rekognition.model.Image
import software.amazon.awssdk.services.rekognition.model.ModerationLabel
import software.amazon.awssdk.services.rekognition.model.S3Object

@Service
class RekognitionService(
    private val rekognitionClient: RekognitionClient,
    private val s3ImgPathProperties: S3ImgPathProperties,
) {
    @Value("\${spring.cloud.aws.s3.img.bucket}")
    private lateinit var bucket: String

    fun getModerationLabels(build: DetectModerationLabelsRequest): List<ModerationLabel> {
        return rekognitionClient.detectModerationLabels(build).moderationLabels()
    }

    fun isModeratingCardImg(imgName: String): Boolean =
        isModeratingImg(s3ImgPathProperties.userCardImg, imgName)

    fun isModeratingImg(filePath: String, imgName: String): Boolean {
        val build = DetectModerationLabelsRequest.builder()
            .minConfidence(50F)
            .image(
                Image.builder()
                    .s3Object(
                        S3Object.builder()
                            .bucket(bucket)
                            .name(filePath + imgName)
                            .build()
                    )
                    .build()
            )
            .build()

        val moderationLabels = getModerationLabels(build)
        return moderationLabels.any { label ->
            isExplicitNudity(label) || isExplicitSexualActivity(label) || isSexToys(label) || isGraphicViolence(label)
        }
    }

    private companion object {
        private fun isSexToys(label: ModerationLabel): Boolean {
            return label.name().equals("Sex Toys", ignoreCase = true) && label.taxonomyLevel() == 2
        }

        private fun isExplicitSexualActivity(label: ModerationLabel): Boolean {
            return label.name().equals("Explicit Sexual Activity", ignoreCase = true) && label.taxonomyLevel() == 2
        }

        private fun isExplicitNudity(label: ModerationLabel): Boolean {
            return label.name().equals("Explicit Nudity", ignoreCase = true) && label.taxonomyLevel() == 2
        }

        private fun isGraphicViolence(label: ModerationLabel): Boolean {
            return label.name().equals("Graphic Violence", ignoreCase = true) && label.taxonomyLevel() == 2
        }
    }
}