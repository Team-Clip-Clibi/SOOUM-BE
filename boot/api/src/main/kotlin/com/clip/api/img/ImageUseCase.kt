package com.clip.api.img

import com.clip.api.img.controller.dto.ImageUploadRequest
import com.clip.api.img.controller.dto.ImageUrlInfoResponse
import com.clip.data.img.service.ProfileImgService
import com.clip.infra.s3.S3ImgPathProperties
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ImageUseCase(
    private val s3ImgService: S3ImgService,
    private val s3ImgPathProperties: S3ImgPathProperties,
    private val profileImgService: ProfileImgService
) {
    fun createProfileUploadUrlAndSave(imageUploadRequest: ImageUploadRequest): ImageUrlInfoResponse {
        if (!imageUploadRequest.extension.equals("jpeg", ignoreCase = true)) {
            throw UnsupportedOperationException("지원하지 않는 확장자입니다.")
        }
        val imgName = "${UUID.randomUUID()}.$imageUploadRequest.extension"

        profileImgService.saveProfileImg(imgName)
        val uploadUrl = s3ImgService.generatePutPresignedUrl(
            filePath = s3ImgPathProperties.profileImg,
            imgName = imgName,
        )

        return ImageUrlInfoResponse(
            imgName = imgName,
            imgUrl = uploadUrl,
        )
    }

}