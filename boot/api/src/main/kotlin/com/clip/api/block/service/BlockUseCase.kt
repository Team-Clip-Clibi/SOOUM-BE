package com.clip.api.block.service

import com.clip.api.block.controller.dto.BlockInfoDto
import com.clip.data.block.service.BlockMemberService
import com.clip.data.follow.service.FollowService
import com.clip.data.member.service.MemberService
import com.clip.global.exception.IllegalStateException
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BlockUseCase(
    private val memberService: MemberService,
    private val followService: FollowService,
    private val blockMemberService: BlockMemberService,
    private val s3ImgService: S3ImgService
) {

    @Transactional
    fun saveBlockMember(fromMemberId: Long, toMemberId: Long) {
        runCatching {
            followService.deleteFollow(
                memberService.findMember(fromMemberId),
                memberService.findMember(toMemberId)
            )
            blockMemberService.saveBlockMember(fromMemberId, toMemberId)
        }.onFailure {
            throw IllegalStateException.AlreadyCompletedException("이미 차단된 사용자입니다.")
        }

    }


    @Transactional
    fun deleteBlockMember(fromMemberId: Long, toMemberId: Long) =
        blockMemberService.deleteBlockMember(fromMemberId, toMemberId)

    @Transactional(readOnly = true)
    fun getBlockMemberList(fromMemberId: Long, lastBlockId: Long?): List<BlockInfoDto> {
        return blockMemberService.getBlockMemberList(fromMemberId, Optional.ofNullable(lastBlockId))
            .map {
                BlockInfoDto(
                    blockId = it.pk,
                    blockMemberId = it.toMember.pk,
                    blockMemberNickname = it.toMember.nickname,
                    blockMemberProfileImageUrl = it.toMember.profileImgName?.let { profileName ->
                        s3ImgService.generateProfileImgUrl(profileName)
                    }
                )
            }

    }
}