package com.clip.api.img.controller

import com.clip.api.docs.img.ImageDocs
import com.clip.api.img.ImageUseCase
import com.clip.api.img.dto.ImageUrlInfoResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageUseCase: ImageUseCase
): ImageDocs {

    @PostMapping("/profile")
    override fun uploadProfileImage(@RequestParam extension: String): ResponseEntity<ImageUrlInfoResponse> {
        val response = imageUseCase.createProfileUploadUrlAndSave(extension)
        return ResponseEntity.ok(response)
    }
}