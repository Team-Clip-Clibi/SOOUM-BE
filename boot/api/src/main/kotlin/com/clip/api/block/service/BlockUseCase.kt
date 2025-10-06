package com.clip.api.block.service

import com.clip.data.block.service.BlockMemberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BlockUseCase(
    private val blockMemberService: BlockMemberService,
) {

    @Transactional
    fun saveBlockMember(fromMemberId: Long, toMemberId: Long) =
        blockMemberService.saveBlockMember(fromMemberId, toMemberId)

    @Transactional
    fun deleteBlockMember(fromMemberId: Long, toMemberId: Long) =
        blockMemberService.deleteBlockMember(fromMemberId, toMemberId)
}