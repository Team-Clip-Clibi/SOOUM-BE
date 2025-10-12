package com.clip.api.img.service

import com.clip.api.img.controller.dto.DefaultImagesResponse
import com.clip.api.img.controller.dto.DefaultImgCategory
import com.clip.api.img.controller.dto.ImageUrlInfoResponse
import com.clip.api.img.controller.dto.ImgInfo
import com.clip.api.img.controller.dto.VerificationResultResponse
import com.clip.data.img.service.CardImgService
import com.clip.data.img.service.ProfileImgService
import com.clip.global.exception.ImageException
import com.clip.infra.rekognition.RekognitionService
import com.clip.infra.s3.S3ImgPathProperties
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ImageUseCase(
    private val s3ImgService: S3ImgService,
    private val s3ImgPathProperties: S3ImgPathProperties,
    private val profileImgService: ProfileImgService,
    private val defaultImageProperties: DefaultImageProperties,
    private val cardImgService: CardImgService,
    private val rekognitionService: RekognitionService,
) {

    fun createUserCardImgUploadUrlAndSave() : ImageUrlInfoResponse {
        val imgName = "${UUID.randomUUID()}.jpeg"

        cardImgService.saveCardImg(imgName)

        val uploadUrl = s3ImgService.generatePutPresignedUrl(
            filePath = s3ImgPathProperties.userCardImg,
            imgName = imgName,
        )

        return ImageUrlInfoResponse(
            imgName = imgName,
            imgUrl = uploadUrl,
        )
    }

    fun createProfileUploadUrlAndSave(): ImageUrlInfoResponse {
        val imgName = "${UUID.randomUUID()}.jpeg"

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

    fun findDefaultImageInfos(): DefaultImagesResponse {

        return DefaultImagesResponse(
            mapOf(
                DefaultImgCategory.ABSTRACT to defaultImageProperties.abstract.map {
                    ImgInfo(
                        it,
                        s3ImgService.generateDefaultCardImgUrl(it)
                    )
                } ,DefaultImgCategory.NATURE to defaultImageProperties.nature.map {
                    ImgInfo(
                        it,
                        s3ImgService.generateDefaultCardImgUrl(it)
                    )
                } ,
                DefaultImgCategory.SENSITIVITY to defaultImageProperties.sensitivity.map {
                    ImgInfo(
                        it,
                        s3ImgService.generateDefaultCardImgUrl(it)
                    )
                } ,
                DefaultImgCategory.FOOD to defaultImageProperties.food.map {
                    ImgInfo(
                        it,
                        s3ImgService.generateDefaultCardImgUrl(it)
                    )
                } ,
                DefaultImgCategory.COLOR to defaultImageProperties.color.map {
                    ImgInfo(
                        it,
                        s3ImgService.generateDefaultCardImgUrl(it)
                    )
                },
                DefaultImgCategory.MEMO to defaultImageProperties.memo.map {
                    ImgInfo(
                        it,
                        s3ImgService.generateDefaultCardImgUrl(it)
                    )
                }
            )
        )
    }

    fun getVerificationCardImgResult(imgName: String): VerificationResultResponse {
        if (!s3ImgService.isCardImgSaved(imgName))
            throw ImageException.ImageNotFoundException(imgName = imgName)
        if (rekognitionService.isModeratingCardImg(imgName))
            return VerificationResultResponse(false)
        return VerificationResultResponse(true)
    }

}