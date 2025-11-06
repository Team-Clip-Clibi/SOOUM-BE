package com.clip.api.version.service

import com.clip.api.version.controller.dto.AppVersionStatus
import com.clip.api.version.controller.dto.AppVersionStatusEnum
import com.clip.api.version.controller.dto.AppVersionStatusResponse
import com.clip.data.member.entity.DeviceType
import com.clip.data.version.service.AppVersionService
import org.springframework.stereotype.Service

@Service
class AppVersionUseCase(
    private val appVersionService: AppVersionService,
) {
    fun checkVersion(type: DeviceType, version: String): AppVersionStatusResponse {
        val appVersion = appVersionService.getAppVersionByDeviceType(type)

        return when {
            isPendingVersion(appVersion.pendingVersion, appVersion.latestVersion, version) ->
                AppVersionStatusResponse(AppVersionStatusEnum.PENDING, appVersion.latestVersion)
            isUpdateVersion(appVersion.minVersion, version) ->
                AppVersionStatusResponse(AppVersionStatusEnum.UPDATE, appVersion.latestVersion)
            else -> AppVersionStatusResponse(AppVersionStatusEnum.OK, appVersion.latestVersion)
        }
    }

    private fun isPendingVersion(pendingVersion: String, latestVersion: String, clientVersion: String) =
        compareVersionParts(latestVersion, pendingVersion) == 1 &&
                compareVersionParts(pendingVersion, clientVersion) == 0

    private fun isUpdateVersion(minVersion: String, clientVersion: String) =
        compareVersionParts(minVersion, clientVersion) == -1

    private fun compareVersionParts(referenceVersion: String, targetVersion: String): Int {
        val referenceParts = referenceVersion.toVersionParts()
        val targetParts = targetVersion.toVersionParts()
        for (i in 0 until maxOf(referenceParts.size, targetParts.size)) {
            val referencePart = referenceParts.getOrElse(i) { 0 }
            val targetPart = targetParts.getOrElse(i) { 0 }
            if (targetPart < referencePart) return -1
            if (targetPart > referencePart) return 1
        }
        return 0
    }

    private fun String.toVersionParts(): List<Int> =
        split(".").map { it.toInt() }


    /*
     *  iOS 레거시 버전 관리
     */
    fun findIosLatestVersion(): String {
        return appVersionService.iosLatestVersion
    }

    fun findIosVersionStatus(version: String): AppVersionStatus {
        val appVersion = appVersionService.getAppVersionByDeviceType(DeviceType.IOS)
        return when {
            isPendingVersion(appVersion.pendingVersion, appVersion.latestVersion, version) -> AppVersionStatus.PENDING
            isUpdateVersion(appVersion.minVersion, version) -> AppVersionStatus.UPDATE
            else -> AppVersionStatus.OK
        }
    }



}