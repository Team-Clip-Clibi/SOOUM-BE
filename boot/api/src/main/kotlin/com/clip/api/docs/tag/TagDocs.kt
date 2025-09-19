package com.clip.api.docs.tag

import com.clip.api.tag.controller.dto.RelatedTagRequest
import com.clip.api.tag.controller.dto.TagInfoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Tag", description = "Tag API")
interface TagDocs {

    @Operation(summary = "연관 태그 검색 API", description = """
        - 요청 Tag와 연관된 Tag 검색 API
        - 검색 결과 개수 옵션은 20개까지 설정 가능합니다.
        - 요청 Body의 tag 값이 null이거나 빈 문자열인 경우 204 No Content를 반환합니다.
        - 검색된 Tag가 없는 경우 204 No Content를 반환합니다.
    """)
    fun getRelatedTags(relatedTagRequest: RelatedTagRequest, resultCnt: Int): ResponseEntity<TagInfoResponse>
}