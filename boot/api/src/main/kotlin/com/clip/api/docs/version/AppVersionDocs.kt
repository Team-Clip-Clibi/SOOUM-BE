package com.clip.api.docs.version

import com.clip.api.version.controller.dto.AppVersionStatusResponse
import com.clip.data.member.entity.DeviceType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "AppVersion", description = "AppVersion API")
interface  AppVersionDocs {

    @Operation(summary = "앱 버전 확인 API", description = """
        - 앱 버전 확인 API
        - 값은 PENDING,UPDATE,OK가 존재하며, UPDATE일 경우에만 강제 업데이트를 진행합니다.
    """)
    fun checkVersion(type: DeviceType, version: String): ResponseEntity<AppVersionStatusResponse>
}