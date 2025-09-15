package com.clip.api.notification.controller

import com.clip.api.docs.notification.NotificationDocs
import com.clip.api.notification.controller.dto.NotificationDto
import com.clip.api.notification.service.NotificationUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationUseCase: NotificationUseCase
): NotificationDocs {

    @PatchMapping("/{id}/read")
    override fun updateNotificationToRead(
        @PathVariable id: Long,
        @AccessUser userId: Long
    ): ResponseEntity<Void> =
        notificationUseCase.updateNotificationStatusToRead(id, userId)
            .let { ResponseEntity.ok().build() }


    @GetMapping("/unread", "/unread/{lastId}")
    override fun findUnreadNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.CommonNotificationInfo>> {
        val findUnreadNotifications = notificationUseCase.findUnreadNotifications(lastId, userId)
        if (findUnreadNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findUnreadNotifications)
    }

    @GetMapping("/read", "/read/{lastId}")
    override fun findReadNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.CommonNotificationInfo>> {
        val findReadNotifications = notificationUseCase.findReadNotifications(lastId, userId)
        if (findReadNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findReadNotifications)
    }

    @GetMapping("/unread/card", "/unread/card/{lastId}")
    override fun findUnreadCardNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> {
        val findUnreadCardNotifications = notificationUseCase.findUnreadCardNotifications(lastId, userId)
        if (findUnreadCardNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findUnreadCardNotifications)
    }

    @GetMapping("/read/card", "/read/card/{lastId}")
    override fun findReadCardNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> {
        val findReadCardNotifications = notificationUseCase.findReadCardNotifications(lastId, userId)
        if (findReadCardNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findReadCardNotifications)
    }

    @GetMapping("/unread/follow", "/unread/follow/{lastId}")
    override fun findUnreadFollowNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> {
        val findUnreadFollowNotifications = notificationUseCase.findUnreadFollowNotifications(lastId, userId)
        if (findUnreadFollowNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findUnreadFollowNotifications)
    }

    @GetMapping("/read/follow", "/read/follow/{lastId}")
    override fun findReadFollowNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ) : ResponseEntity<List<NotificationDto.NotificationInfoResponse>> {
        val findReadFollowNotifications = notificationUseCase.findReadFollowNotifications(lastId, userId)
        if (findReadFollowNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findReadFollowNotifications)
    }

    @GetMapping("/unread/notice", "/unread/notice/{lastId}")
    override fun findUnreadNoticeNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> {
        val findUnreadNoticeNotifications = notificationUseCase.findUnreadNoticeNotifications(lastId, userId)
        if (findUnreadNoticeNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findUnreadNoticeNotifications)
    }

    @GetMapping("/read/notice", "/read/notice/{lastId}")
    override fun findReadNoticeNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ) : ResponseEntity<List<NotificationDto.NotificationInfoResponse>> {
        val findReadNoticeNotifications = notificationUseCase.findReadNoticeNotifications(lastId, userId)
        if (findReadNoticeNotifications.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(findReadNoticeNotifications)
    }
}