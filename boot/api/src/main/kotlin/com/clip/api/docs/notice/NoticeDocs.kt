package com.clip.api.docs.notice

import com.clip.api.notice.controller.dto.NoticeInfoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Notice", description = "Notice API")
interface NoticeDocs {

    @Operation(summary = "공지 조회 API", description = """
        - 공지사항을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
        - 공지가 없을 경우 204 No Content를 반환합니다.
        - pageSize의 기본값과 최대값은 30입니다.
        - pageSize를 30이하 (예: 3)으로 설정하여 홈 상단의 공지 노출에 사용할 수 있습니다.
    """)
    fun findNotices(lastId: Long?, pageSize: Int,): ResponseEntity<NoticeInfoResponse>
}