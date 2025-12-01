package com.clip.batch.card.repository

import com.clip.data.notification.entity.NotificationHistory
import com.clip.data.notification.repository.NotificationHistoryRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface NotificationBatchRepository : NotificationHistoryRepository {

    @Query("select n from NotificationHistory n where n.targetCardPk in :targetCardPks")
    fun findNotificationsForDeletion(
        @Param("targetCardPks") targetCardPks: List<Long>
    ): List<NotificationHistory>
}