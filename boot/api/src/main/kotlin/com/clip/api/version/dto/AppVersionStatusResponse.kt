package com.clip.api.version.dto

data class AppVersionStatus(
    val status: AppVersionStatusEnum
)

enum class AppVersionStatusEnum {
    PENDING,
    UPDATE,
    OK
}