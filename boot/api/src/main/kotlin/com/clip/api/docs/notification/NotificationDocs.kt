package com.clip.api.docs.notification

import com.clip.api.notification.controller.dto.NotificationDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Notification", description = "Notification API")
interface NotificationDocs {

    @Operation(summary = "알림 읽음 API", description = """
        - 알림을 읽음 상태로 변경하는 API
        - 요청의 알림id에 해당하는 알림을 읽음 상태로 변경합니다.
    """)
    fun updateNotificationToRead(id: Long, userId: Long): ResponseEntity<Void>

    @Operation(summary = "읽지 않은 전체 알림 조회 API", description = """
        - 전체 필터의 읽지 않은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
        - 더이상 데이터가 없을 경우 204 No Content를 반환합니다.
    """)
    fun findUnreadNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.CommonNotificationInfo>>

    @Operation(summary = "읽은 공지사항 알림 조회 API", description = """
        - 공지사항 필터의 읽은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
        - 더이상 데이터가 없을 경우 204 No Content를 반환합니다.
    """)
    fun findReadNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.CommonNotificationInfo>>
}