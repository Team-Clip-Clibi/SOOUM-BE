package com.clip.api.rsa.controller

import com.clip.api.docs.rsa.RsaDocs
import com.clip.api.rsa.dto.KeyInfoResponse
import com.clip.api.rsa.service.RsaUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rsa")
class RsaController(
    private val rsaUsecase: RsaUseCase
): RsaDocs {

    @GetMapping("/public-key")
    override fun getPublicKey(): ResponseEntity<KeyInfoResponse> {
        val publicKey = rsaUsecase.getPublicKey()
        return ResponseEntity.ok(KeyInfoResponse(publicKey))
    }

}