package com.clip.api.img.controller

import com.clip.api.docs.img.ImageDocs
import com.clip.api.img.ImageUseCase
import com.clip.api.img.controller.dto.ImageUploadRequest
import com.clip.api.img.controller.dto.ImageUrlInfoResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageUseCase: ImageUseCase
): ImageDocs {

    @PostMapping("/profile")
    override fun uploadProfileImage(@RequestBody imageUploadRequest: ImageUploadRequest): ResponseEntity<ImageUrlInfoResponse> {
        val response = imageUseCase.createProfileUploadUrlAndSave(imageUploadRequest)
        return ResponseEntity.ok(response)
    }
}