package com.clip.api.member.controller.dto

import com.clip.data.member.entity.DeviceType

data class SignUpRequest (
    val memberInfo: MemberInfo,
    val policy: Policy,
)

data class MemberInfo(
    val encryptedDeviceId: String,
    val deviceType: DeviceType,
    val deviceModel: String,
    val deviceOsVersion: String,
    val fcmToken: String?,
    val isNotificationAgreed: Boolean,
    val nickname: String?,
    val profileImage: String?,
)

data class Policy(
    val agreedToTermsOfService: Boolean,
    val agreedToLocationTerms: Boolean,
    val agreedToPrivacyPolicy: Boolean,
)