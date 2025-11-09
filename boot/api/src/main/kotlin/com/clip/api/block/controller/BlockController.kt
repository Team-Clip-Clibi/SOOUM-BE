package com.clip.api.block.controller

import com.clip.api.block.controller.dto.BlockInfoDto
import com.clip.api.block.service.BlockUseCase
import com.clip.api.docs.block.BlockDocs
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blocks")
class BlockController(
    private val blockUseCase: BlockUseCase,
): BlockDocs {

    @PostMapping("/{toMemberId}")
    override fun saveBlockMember(
        @PathVariable toMemberId: Long,
        @AccessUser fromMemberId: Long
    ): ResponseEntity<Void> =
        blockUseCase.saveBlockMember(fromMemberId, toMemberId)
            .let { ResponseEntity.ok().build() }

    @DeleteMapping("/{toMemberId}")
    override fun deleteBlockMember(
        @PathVariable toMemberId: Long,
        @AccessUser fromMemberId: Long
    ): ResponseEntity<Void> =
        blockUseCase.deleteBlockMember(fromMemberId, toMemberId)
            .let { ResponseEntity.ok().build() }

    @GetMapping( "", "/{lastBlockId}")
    override fun getBlockMemberList(@AccessUser fromMemberId: Long, @PathVariable(required = false) lastBlockId: Long?): ResponseEntity<List<BlockInfoDto>> =
        blockUseCase.getBlockMemberList(fromMemberId, lastBlockId)
            .takeIf { it.isNotEmpty() }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()


}