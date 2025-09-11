package com.clip.api.version.dto

data class AppVersionStatusResponse(
    val status: AppVersionStatusEnum
)

enum class AppVersionStatusEnum {
    PENDING,
    UPDATE,
    OK
}