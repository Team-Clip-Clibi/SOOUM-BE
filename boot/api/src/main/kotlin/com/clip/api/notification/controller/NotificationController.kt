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
}