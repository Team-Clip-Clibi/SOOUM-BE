package com.clip.api.notice.controller

import com.clip.api.docs.notice.NoticeDocs
import com.clip.api.notice.controller.dto.NoticeInfoResponse
import com.clip.api.notice.service.NoticeUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class NoticeController(
    private val noticeUseCase: NoticeUseCase,
): NoticeDocs {

    @GetMapping("/notices", "/notices/{lastId}")
    override fun findNotices(
        @PathVariable(required = false) lastId: Long?,
    ): ResponseEntity<NoticeInfoResponse> {
        val notices = noticeUseCase.findNotices(lastId)
        if (notices.notices.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(notices)
    }
}