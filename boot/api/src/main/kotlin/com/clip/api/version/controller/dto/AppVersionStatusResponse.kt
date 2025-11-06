package com.clip.api.version.controller.dto

data class AppVersionStatusResponse(
    val status: AppVersionStatusEnum,
    val latestVersion: String
)

enum class AppVersionStatusEnum {
    PENDING,
    UPDATE,
    OK
}