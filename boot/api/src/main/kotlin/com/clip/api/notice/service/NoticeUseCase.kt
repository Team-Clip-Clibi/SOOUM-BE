package com.clip.api.notice.service

import com.clip.api.notice.controller.dto.NoticeInfo
import com.clip.api.notice.controller.dto.NoticeInfoResponse
import com.clip.api.notice.controller.dto.NoticeSource
import com.clip.data.notice.service.NoticeService
import org.springframework.stereotype.Service

@Service
class NoticeUseCase(
    private val noticeService: NoticeService
) {

    fun findNotices(lastId: Long?, source: NoticeSource, pageSize: Int) =
        when (source) {
            NoticeSource.NOTIFICATION -> noticeService.findNoticesForNotification(lastId, pageSize)
            NoticeSource.SETTINGS -> noticeService.findNoticesForSettings(lastId, pageSize)
        }.map {
            NoticeInfo(
                id = it.pk,
                title = it.title,
                url = it.url,
                createdAt = it.date,
                noticeType = it.noticeType
            )
        }.let(::NoticeInfoResponse)

}