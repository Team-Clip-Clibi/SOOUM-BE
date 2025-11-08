package com.clip.api.docs.tag

import com.clip.api.tag.controller.dto.RelatedTagRequest
import com.clip.api.tag.controller.dto.TagInfoResponse
import com.clip.global.security.annotation.AccessUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "Tag", description = "Tag API")
interface TagDocs {

    @Operation(summary = "연관 태그 검색 API", description = """
        - 요청 Tag와 연관된 Tag 검색 API
        - 검색 결과 개수 옵션은 20개까지 설정 가능합니다.
        - 요청 Body의 tag 값이 null이거나 빈 문자열인 경우 204 No Content를 반환합니다.
        - 검색된 Tag가 없는 경우 204 No Content를 반환합니다.
    """)
    fun getRelatedTags(relatedTagRequest: RelatedTagRequest, resultCnt: Int): ResponseEntity<TagInfoResponse>


    @Operation(summary = "Top 10 tag 조회 API", description = """
        - 사용량이 많은 태그 상위 10개를 조회합니다.
    """)
    fun getTop10Tags(): ResponseEntity<TagInfoResponse>

    @Operation(summary = "태그 즐겨찾기 API", description = """
        - 요청 태그id를 사용자의 즐겨찾기 태그로 등록하는 API
        - 즐겨찾기는 최대 9개까지 가능합니다.
        - 즐겨찾기 최대 개수를 초과하는 요청은 400 Bad Request를 반환합니다.
        - 이미 즐겨찾기에 등록된 태그를 다시 등록하는 요청은 409 Conflict를 반환합니다.
    """)
    fun createFavoriteTag(
        @PathVariable tagId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<Unit>

    @Operation(summary = "태그 즐겨찾기 취소 API", description = """
        - 요청 태그id를 사용자의 즐겨찾기 태그에서 제거하는 API
    """)
    fun deleteFavoriteTag(
        @PathVariable tagId: Long,
        @AccessUser userId: Long
    ): ResponseEntity<Unit>
}