package com.clip.api.block.controller.dto

data class BlockInfoDto(
    val blockId: Long,
    val blockMemberId: Long,
    val blockMemberNickname: String,
    val blockMemberProfileImageUrl: String?,
) {
}