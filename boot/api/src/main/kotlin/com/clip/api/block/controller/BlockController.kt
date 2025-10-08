package com.clip.api.block.controller

import com.clip.api.block.service.BlockUseCase
import com.clip.api.docs.block.BlockDocs
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        blockUseCase.saveBlockMember(toMemberId, fromMemberId)
            .let { ResponseEntity.ok().build() }

    @DeleteMapping("/{toMemberId}")
    override fun deleteBlockMember(
        @PathVariable toMemberId: Long,
        @AccessUser fromMemberId: Long
    ): ResponseEntity<Void> =
        blockUseCase.deleteBlockMember(toMemberId, fromMemberId)
            .let { ResponseEntity.ok().build() }

}