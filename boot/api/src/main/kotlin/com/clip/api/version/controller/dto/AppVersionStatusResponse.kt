package com.clip.api.version.controller.dto

data class AppVersionStatusResponse(
    val status: AppVersionStatusEnum
)

enum class AppVersionStatusEnum {
    PENDING,
    UPDATE,
    OK
}