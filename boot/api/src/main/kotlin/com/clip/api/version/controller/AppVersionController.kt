package com.clip.api.version.controller

import com.clip.api.docs.version.AppVersionDocs
import com.clip.api.version.dto.AppVersionStatus
import com.clip.api.version.dto.AppVersionStatusResponse
import com.clip.api.version.service.AppVersionUseCase
import com.clip.data.version.entity.DeviceType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/version")
class AppVersionController(
    private val appVersionUseCase: AppVersionUseCase
) : AppVersionDocs {

    @GetMapping("/{type}")
    override fun checkVersion(
        @PathVariable type: DeviceType,
        @RequestParam version: String
    ): ResponseEntity<AppVersionStatusResponse> {
        return ResponseEntity.ok(appVersionUseCase.checkVersion(type, version))
    }
}