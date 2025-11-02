package com.clip.api.block.service

import com.clip.data.block.service.BlockMemberService
import com.clip.global.exception.IllegalStateException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BlockUseCase(
    private val blockMemberService: BlockMemberService,
) {

    @Transactional
    fun saveBlockMember(fromMemberId: Long, toMemberId: Long) {
        runCatching {
            blockMemberService.saveBlockMember(fromMemberId, toMemberId)
        }.onFailure {
            throw IllegalStateException.AlreadyCompletedException("이미 차단된 사용자입니다.")
        }

    }


    @Transactional
    fun deleteBlockMember(fromMemberId: Long, toMemberId: Long) =
        blockMemberService.deleteBlockMember(fromMemberId, toMemberId)
}