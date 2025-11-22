package com.clip.api.member.controller

import com.clip.api.docs.member.MemberSettingDocs
import com.clip.api.member.controller.dto.AccountTransferCodeResponse
import com.clip.api.member.controller.dto.TransferAccountRequest
import com.clip.api.member.service.AccountTransferUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberSettingController(
    private val accountTransferUseCase: AccountTransferUseCase
): MemberSettingDocs {

    @GetMapping("/account/transfer-code")
    override fun getAccountTransferCode(@AccessUser userId: Long) : ResponseEntity<AccountTransferCodeResponse> =
        ResponseEntity.ok(accountTransferUseCase.findOrSaveAccountTransferId(userId))

    @PatchMapping("/account/transfer-code")
    override fun updateAccountTransferCode(@AccessUser userId: Long) : ResponseEntity<AccountTransferCodeResponse> =
        ResponseEntity.ok(accountTransferUseCase.updateAccountTransferId(userId))

    @PostMapping ("/account/transfer")
    override fun transferAccount(@RequestBody transferRequest: TransferAccountRequest, @AccessUser userId: Long) {
        accountTransferUseCase.transferMemberAccount(transferRequest)
    }
}