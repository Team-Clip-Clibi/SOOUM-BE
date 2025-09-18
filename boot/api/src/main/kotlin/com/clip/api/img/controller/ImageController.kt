package com.clip.api.img.controller

import com.clip.api.docs.img.ImageDocs
import com.clip.api.img.ImageUseCase
import com.clip.api.img.controller.dto.ImageUrlInfoResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageUseCase: ImageUseCase
): ImageDocs {

    @PostMapping("/profile")
    override fun uploadProfileImage(): ResponseEntity<ImageUrlInfoResponse> {
        val response = imageUseCase.createProfileUploadUrlAndSave()
        return ResponseEntity.ok(response)
    }
}