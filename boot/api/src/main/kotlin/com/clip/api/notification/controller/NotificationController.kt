package com.clip.api.notification.controller

import com.clip.api.notification.controller.dto.NotificationDto
import com.clip.api.notification.service.NotificationUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationUseCase: NotificationUseCase
) {

    @PatchMapping("/{id}/read")
    fun updateNotificationAsRead(
        @PathVariable id: Long,
        @AccessUser userId: Long
    ): ResponseEntity<Void> =
        notificationUseCase.updateNotificationStatusToRead(id, userId)
            .let { ResponseEntity.ok().build() }


    @GetMapping("/unread", "/unread/{lastId}")
    fun findUnreadNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.CommonNotificationInfo>> =
        notificationUseCase.findUnreadNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }


    @GetMapping("/unread/card", "/unread/card/{lastId}")
    fun findUnreadCardNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> =
        notificationUseCase.findUnreadCardNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }


    @GetMapping("/unread/follow", "/unread/follow/{lastId}")
    fun findUnreadFollowNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> =
        notificationUseCase.findUnreadFollowNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }


    @GetMapping("/unread/notice", "/unread/notice/{lastId}")
    fun findUnreadNoticeNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> =
        notificationUseCase.findUnreadNoticeNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }

    @GetMapping("/read", "/read/{lastId}")
    fun findReadNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.CommonNotificationInfo>> =
        notificationUseCase.findReadNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }


    @GetMapping("/read/card", "/read/card/{lastId}")
    fun findReadCardNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ): ResponseEntity<List<NotificationDto.NotificationInfoResponse>> =
        notificationUseCase.findReadCardNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }

    @GetMapping("/read/follow", "/read/follow/{lastId}")
    fun findReadFollowNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ) : ResponseEntity<List<NotificationDto.NotificationInfoResponse>> =
        notificationUseCase.findReadFollowNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }

    @GetMapping("/read/notice", "/read/notice/{lastId}")
    fun findReadNoticeNotifications(
        @PathVariable(required = false) lastId: Long?,
        @AccessUser userId: Long
    ) : ResponseEntity<List<NotificationDto.NotificationInfoResponse>> =
        notificationUseCase.findReadNoticeNotifications(lastId, userId)
            .let { ResponseEntity.ok(it) }

}