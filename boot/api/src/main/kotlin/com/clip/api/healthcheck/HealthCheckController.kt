package com.clip.api.healthcheck

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HealthCheckController {

    @GetMapping("/health-check")
    fun healthCheck(): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}