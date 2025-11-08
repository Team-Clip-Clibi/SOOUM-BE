package com.clip.api.tag.event

import com.clip.api.notification.event.MultiFcmEvent
import com.clip.data.member.service.MemberService
import com.clip.data.notification.entity.NotificationHistory
import com.clip.data.notification.service.NotificationHistoryService
import com.clip.data.tag.service.FavoriteTagService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class TagUsageEventListener(
    private val memberService: MemberService,
    private val favoriteTagService: FavoriteTagService,
    private val notificationHistoryService: NotificationHistoryService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    companion object {
        const val PAGE_SIZE = 100
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun saveTagUsageNotificationHistory(
        tagUsageEvent: TagUsageEvent
    ) {
        val writer = memberService.findMember(tagUsageEvent.writerId)
        for(tag in tagUsageEvent.tags) {
            var lastId: Long? = null
            do {
                val favoriteTags = favoriteTagService.findFavoriteTagsByTagIdWithoutUserId(
                    tag.pk,
                    tagUsageEvent.writerId,
                    tagUsageEvent.cardId,
                    lastId,
                    PageRequest.ofSize(PAGE_SIZE)
                )
                if (favoriteTags.isEmpty()) break
                lastId = favoriteTags.last().pk

                val notificationHistories = favoriteTags.map { favoriteTag ->
                    notificationHistoryService.save(
                        NotificationHistory.ofFavoriteTagUsage(
                            writer,
                            favoriteTag.member,
                            tagUsageEvent.cardId,
                            tag.content
                        )
                    )
                }.toList()

                applicationEventPublisher.publishEvent(MultiFcmEvent(
                    tagUsageEvent.cardId,
                    tag.content,
                    notificationHistories
                ))
            } while (favoriteTags.size == PAGE_SIZE)
        }
    }
}