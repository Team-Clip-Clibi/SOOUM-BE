package com.clip.api.version.service

import com.clip.api.version.dto.AppVersionStatus
import com.clip.api.version.dto.AppVersionStatusEnum
import com.clip.data.version.entity.DeviceType
import com.clip.data.version.service.AppVersionService
import org.springframework.stereotype.Service

@Service
class AppVersionUseCase(
    private val appVersionService: AppVersionService,
) {
    fun checkVersion(type: DeviceType, version: String): AppVersionStatus {
        val appVersion = appVersionService.getAppVersionByDeviceType(type)

        return when {
            isPendingVersion(appVersion.pendingVersion, appVersion.latestVersion, version) -> AppVersionStatus(AppVersionStatusEnum.PENDING)
            isUpdateVersion(appVersion.minVersion, version) -> AppVersionStatus(AppVersionStatusEnum.UPDATE)
            else -> AppVersionStatus(AppVersionStatusEnum.OK)
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
}