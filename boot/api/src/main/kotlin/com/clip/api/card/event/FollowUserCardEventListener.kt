package com.clip.api.card.event

import com.clip.api.notification.event.MultiFcmFollowerCardUploadEvent
import com.clip.data.follow.service.FollowService
import com.clip.infra.s3.S3ImgPathProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.util.*

@Component
class FollowUserCardEventListener(
    private val S3ImgPathProperties: S3ImgPathProperties,
    @Value("\${spring.cloud.aws.s3.img.bucket}") private val bucket: String,
    private val followService: FollowService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    companion object {
        const val PAGE_SIZE = 50
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendFollowerCardUploadNotification(
        followUserCardEvent: FollowUserCardEvent
    ) {
        var lastId: Long? = null
        do {
            val followers = followService.findFollowerWithoutBlockedMembers(
                Optional.ofNullable(lastId),
                followUserCardEvent.cardCreatorId,
                emptyList()
            ).map { follower -> follower.fromMember }.toList()
            if (followers.isEmpty()) break

            lastId = followers.last().pk

            val targetUsers = followers.filter { follower-> follower.isAllowFollowUserCardNotify }.toList()
            applicationEventPublisher.publishEvent(
                MultiFcmFollowerCardUploadEvent(
                    followUserCardEvent.cardId,
                    followUserCardEvent.content,
                    followUserCardEvent.nickname,
                    "https://${bucket}.amazonaws.com/${S3ImgPathProperties.userCardImg}${followUserCardEvent.userImgName}",
                    targetUsers
                )
            )
        }while (followers.size == PAGE_SIZE)
    }
}