package com.clip.api.docs.block

import com.clip.api.block.controller.dto.BlockInfoDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Block", description = "차단 API")
interface BlockDocs {

    @Operation(summary = "차단 API", description = """
        - toMemberId에 해당하는 유저를 차단합니다.
    """)
    fun saveBlockMember(toMemberId: Long, fromMemberId: Long): ResponseEntity<Void>

    @Operation(summary = "차단 해제 API", description = """
        - toMemberId에 해당하는 유저의 차단을 해제합니다.
    """)
    fun deleteBlockMember(toMemberId: Long, fromMemberId: Long): ResponseEntity<Void>

    @Operation(summary = "차단 목록 조회 API", description = """
        - 차단한 유저들의 목록을 조회합니다.
    """)
    fun getBlockMemberList(fromMemberId: Long, lastBlockId: Long?): ResponseEntity<List<BlockInfoDto>>
}