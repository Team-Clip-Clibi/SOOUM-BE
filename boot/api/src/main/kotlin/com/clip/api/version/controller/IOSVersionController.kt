package com.clip.api.version.controller

import com.clip.api.version.dto.AppVersionStatus
import com.clip.api.version.service.AppVersionUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/*
 * 현재 운영 중인 iOS 앱 사용자들을 위한 레거시 버전 관리 컨트롤러
 */
@RestController
@RequestMapping("/app/version")
class IOSVersionController(
    private val appVersionUseCase: AppVersionUseCase,
) {
    @GetMapping("/ios")
    fun getIOSVersion(): ResponseEntity<String> {
        return ResponseEntity.ok(appVersionUseCase.findIosLatestVersion())
    }

    @GetMapping("/ios/v2")
    fun getIOSVersionV2(@RequestParam version: String): ResponseEntity<AppVersionStatus> {
        return ResponseEntity.ok(appVersionUseCase.findIosVersionStatus(version))
    }
}