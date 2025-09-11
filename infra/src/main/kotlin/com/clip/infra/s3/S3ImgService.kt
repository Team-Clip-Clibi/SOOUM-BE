package com.clip.infra.s3
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class S3ImgService(
    private val s3Presigner: S3Presigner,
    private val s3Client: S3Client,
    @Value("\${spring.cloud.aws.s3.img.bucket}") private val bucket: String
) {
    companion object {
        private val EXPIRY_TIME: Duration = Duration.ofHours(24)
    }

    fun generateGetPresignedUrl(imgPathPrefix: String, imgName: String): String =
        s3Presigner.presignGetObject(
            GetObjectPresignRequest.builder()
                .getObjectRequest(
                    GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(imgPathPrefix + imgName)
                        .build()
                )
                .signatureDuration(EXPIRY_TIME)
                .build()
        ).url().toString()

    fun generatePutPresignedUrl(filePath: String, imgName: String): String =
        s3Presigner.presignPutObject(
            PutObjectPresignRequest.builder()
                .putObjectRequest(
                    PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(filePath + imgName)
                        .build()
                )
                .signatureDuration(EXPIRY_TIME)
                .build()
        ).url().toString()

    /**
     * s3에 사진이 존재하면 true, 아니면 false
     */
    fun isImgSaved(filePath: String, imgName: String): Boolean =
        try {
            s3Client.headObject(
                HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(filePath + imgName)
                    .build()
            )
            true
        } catch (e: Exception) {
            false
        }

    fun deleteImgs(filePath: String, imgsName: List<String>) {
        val deleteObjects = imgsName.map { imgName ->
            ObjectIdentifier.builder()
                .key(filePath + imgName)
                .build()
        }
        val del = Delete.builder().objects(deleteObjects).build()
        val multiObjectDeleteRequest = DeleteObjectsRequest.builder()
            .bucket(bucket)
            .delete(del)
            .build()
        s3Client.deleteObjects(multiObjectDeleteRequest)
    }
}
