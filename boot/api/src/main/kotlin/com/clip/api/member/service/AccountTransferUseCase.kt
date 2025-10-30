package com.clip.api.member.service

import com.clip.api.member.controller.dto.AccountTransferCodeResponse
import com.clip.api.member.controller.dto.TransferAccountRequest
import com.clip.api.notification.event.SystemFCMEvent
import com.clip.api.rsa.service.RsaUseCase
import com.clip.data.member.entity.AccountTransferHistory
import com.clip.data.member.entity.Blacklist
import com.clip.data.member.service.*
import com.clip.data.notification.entity.notificationtype.NotificationType
import com.clip.global.security.jwt.JwtProvider
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountTransferUseCase(
    private val accountTransferService: AccountTransferService,
    private val rsaUseCase: RsaUseCase,
    private val memberService: MemberService,
    private val memberWithdrawalUseCase: MemberWithdrawalUseCase,
    private val refreshTokenService: RefreshTokenService,
    private val blackListService: BlacklistService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val accountTransferHistoryService: AccountTransferHistoryService,
    private val jwtProvider: JwtProvider
) {

    @Transactional
    fun findOrSaveAccountTransferId(memberPk: Long): AccountTransferCodeResponse {
        val accountTransfer = accountTransferService.getByMemberPk(memberPk)
            .orElseGet { accountTransferService.saveAccountTransfer(memberPk) }

        if (accountTransfer.isExpired) {
            accountTransfer.updateTransferId(accountTransferService.createTransferId())
        }

        return AccountTransferCodeResponse(
            transferCode = accountTransfer.transferId
        )
    }

    @Transactional
    fun updateAccountTransferId(memberPk: Long): AccountTransferCodeResponse {
        val accountTransfer = accountTransferService.findAccountTransfer(memberPk)

        accountTransfer.updateTransferId(accountTransferService.createTransferId())

        return AccountTransferCodeResponse(
            transferCode = accountTransfer.transferId
        )
    }

    @Transactional
    fun transferMemberAccount(transferRequest: TransferAccountRequest, memberPk: Long) {
        val accountTransfer = accountTransferService.findAvailableAccountTransfer(transferRequest.transferCode)
        val transferredMember = accountTransfer.member
        val decryptedRequesterDeviceId = rsaUseCase.decodeDeviceId(transferRequest.encryptedDeviceId)
        withdrawRequesterIfPresent(decryptedRequesterDeviceId)
        saveTransferredMemberRefreshTokenInBlackList(transferredMember.pk)

        transferredMember.updateDeviceInfo(
            decryptedRequesterDeviceId,
            transferRequest.deviceType,
            transferRequest.deviceModel,
            transferRequest.deviceOsVersion
        )
        accountTransferService.deleteAccountTransfer(transferredMember.pk)
        accountTransferHistoryService.save(
            AccountTransferHistory(
                transferredMember
            )
        )

        applicationEventPublisher.publishEvent(
            SystemFCMEvent(
                expiredAt = null,
                notificationId = 1L,
                targetDeviceType = transferredMember.deviceType,
                fcmToken = transferredMember.firebaseToken,
                notificationType = NotificationType.TRANSFER_SUCCESS
            )
        )
    }

    private fun withdrawRequesterIfPresent(decryptedDeviceId: String) {
        val requesterOp = memberService.findMemberOp(decryptedDeviceId)
        requesterOp.ifPresent { member ->
            memberWithdrawalUseCase.withdrawMember(member.pk)
        }
    }

    private fun saveTransferredMemberRefreshTokenInBlackList(transferMemberPk: Long) {
        val refreshToken = refreshTokenService.findRefreshToken(transferMemberPk)
        blackListService.save(
            Blacklist(
                refreshToken,
                jwtProvider.getTokenExpiration(refreshToken)
            )
        )
    }


}