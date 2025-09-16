package com.clip.api.docs.rsa

import com.clip.api.rsa.controller.dto.KeyInfoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Rsa", description = "Rsa API")
interface RsaDocs {

    @Operation(summary = "RSA Public Key 제공 API", description = """
        - RSA Public Key 제공 API
        - 클라이언트에서 사용할 RSA Public Key를 제공합니다.
    """)
    fun getPublicKey(): ResponseEntity<KeyInfoResponse>
}