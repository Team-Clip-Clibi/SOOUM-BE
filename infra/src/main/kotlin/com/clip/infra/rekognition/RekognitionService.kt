package com.clip.infra.rekognition

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsRequest
import software.amazon.awssdk.services.rekognition.model.Image
import software.amazon.awssdk.services.rekognition.model.ModerationLabel
import software.amazon.awssdk.services.rekognition.model.S3Object

@Service
class RekognitionService(
    private val rekognitionClient: RekognitionClient
) {
    @Value("\${spring.cloud.aws.s3.img.bucket}")
    private lateinit var bucket: String

    fun getModerationLabels(build: DetectModerationLabelsRequest): List<ModerationLabel> {
        return rekognitionClient.detectModerationLabels(build).moderationLabels()
    }

    fun isModeratingImg(filePath: String, imgName: String): Boolean {
        val build = DetectModerationLabelsRequest.builder()
            .minConfidence(70F)
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
            isExplicitNudity(label) || isExplicitSexualActivity(label) || isSexToys(label)
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
    }
}