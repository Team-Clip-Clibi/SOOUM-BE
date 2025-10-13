package com.clip.api.img.controller

import com.clip.api.docs.img.ImageDocs
import com.clip.api.img.controller.dto.DefaultImagesResponse
import com.clip.api.img.controller.dto.ImageUrlInfoResponse
import com.clip.api.img.controller.dto.VerificationResultResponse
import com.clip.api.img.service.ImageUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageUseCase: ImageUseCase
): ImageDocs {

    @GetMapping("/profile")
    override fun uploadProfileImage(): ResponseEntity<ImageUrlInfoResponse> {
        val response = imageUseCase.createProfileUploadUrlAndSave()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/defaults")
    override fun getDefaultImgs(): ResponseEntity<DefaultImagesResponse> =
        imageUseCase.findDefaultImageInfos()
            .let { ResponseEntity.ok(it) }

    @GetMapping("/card-img")
    override fun getUploadCardImgUrl(): ResponseEntity<ImageUrlInfoResponse> =
        imageUseCase.createUserCardImgUploadUrlAndSave()
            .let { ResponseEntity.ok(it) }

    @GetMapping("/{imgName}/verification")
    override fun getVerificationImagResult(
        @PathVariable imgName: String
    ): ResponseEntity<VerificationResultResponse> =
        imageUseCase.getVerificationCardImgResult(imgName)
            .let { ResponseEntity.ok(it) }

}