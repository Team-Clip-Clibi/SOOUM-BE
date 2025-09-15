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
        - 모든 타입의 알림이 포함됩니다.
    """)
    fun findUnreadNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.CommonNotificationInfo>>

    @Operation(summary = "읽은 공지사항 알림 조회 API", description = """
        - 공지사항 필터의 읽은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
    """)
    fun findReadNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.CommonNotificationInfo>>

    @Operation(summary = "읽지 않은 카드 알림 조회 API", description = """
        - 카드 필터의 읽지 않은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
        - 피드 카드 좋아요, 답 카드 좋아요, 답 카드 작성, 팔로우 타입의 알림을 반환합니다.
    """)
    fun findUnreadCardNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.NotificationInfoResponse>>

    @Operation(summary = "읽은 카드 알림 조회 API", description = """
        - 카드 필터의 읽은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
        - 피드 카드 좋아요, 답 카드 좋아요, 답 카드 작성, 팔로우 타입의 알림을 반환합니다.
    """)
    fun findReadCardNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.NotificationInfoResponse>>

    @Operation(summary = "읽지 않은 팔로우 알림 조회 API", description = """
        - 팔로우 필터의 읽지 않은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
    """)
    fun findUnreadFollowNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.NotificationInfoResponse>>

    @Operation(summary = "읽은 팔로우 알림 조회 API", description = """
        - 팔로우 필터의 읽은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
    """)
    fun findReadFollowNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.NotificationInfoResponse>>

    @Operation(summary = "읽지 않은 공지사항 알림 조회 API", description = """
        - 공지사항 필터의 읽지 않은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
    """)
    fun findUnreadNoticeNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.NotificationInfoResponse>>

    @Operation(summary = "읽지 않은 전체 알림 조회 API", description = """
        - 전체 필터의 읽지 않은 알림을 조회하는 API
        - 응답의 가장 마지막 알림 id를 다음 요청의 lastId로 전달하여 페이징 처리할 수 있습니다.
    """)
    fun findReadNoticeNotifications(lastId: Long?,userId: Long): ResponseEntity<List<NotificationDto.NotificationInfoResponse>>
}