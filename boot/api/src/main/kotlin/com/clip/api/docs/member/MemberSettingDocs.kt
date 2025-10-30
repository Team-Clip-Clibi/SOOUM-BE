package com.clip.api.docs.member

import com.clip.api.member.controller.dto.AccountTransferCodeResponse
import com.clip.api.member.controller.dto.TransferAccountRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Member Setting", description = "Member Setting API")
interface MemberSettingDocs {

    @Operation(summary = "계정 이관 코드 조회 API", description = """
        - 사용자가 자신의 계정 이관 코드를 조회할 수 있도록 합니다.
    """)
    fun getAccountTransferCode(userId: Long) : ResponseEntity<AccountTransferCodeResponse>

    @Operation(summary = "계정 이관 코드 업데이트 API", description = """
        - 사용자가 자신의 계정 이관 코드를 업데이트할 수 있도록 합니다.
    """)
    fun updateAccountTransferCode(userId: Long) : ResponseEntity<AccountTransferCodeResponse>

    @Operation(summary = "계정 이관 API", description = """
        - 사용자가 자신의 계정을 이관할 수 있도록 합니다.
    """)
    fun transferAccount(transferRequest: TransferAccountRequest, userId: Long)
}