package com.clip.api.member.controller.dto

import com.clip.data.member.entity.DeviceType

data class SignUpRequest (
    val memberInfo: MemberInfo,
    val policy: Policy,
)

data class MemberInfo(
    val encryptedDeviceId: String,
    val deviceType: DeviceType,
    val fcmToken: String?,
    val isNotificationAgreed: Boolean,
)

data class Policy(
    val agreedToTermsOfService: Boolean,
    val agreedToLocationTerms: Boolean,
    val agreedToPrivacyPolicy: Boolean,
)