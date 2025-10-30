package com.clip.api.member.controller.dto

import com.clip.data.member.entity.DeviceType

data class TransferAccountRequest(
    val transferCode: String,
    val encryptedDeviceId: String,
    val deviceType: DeviceType,
    val deviceModel: String,
    val deviceOsVersion: String
)