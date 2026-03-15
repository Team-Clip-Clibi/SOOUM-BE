package com.clip.api.card.event

import com.clip.api.notification.event.MultiFcmArticleCardUploadEvent
import com.clip.data.member.service.MemberService
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
class ArticleCardEventListener(
    private val S3ImgPathProperties: S3ImgPathProperties,
    @Value("\${spring.cloud.aws.s3.img.bucket}") private val bucket: String,
    @Value("\${spring.cloud.aws.region}") private val region: String,
    private val memberService: MemberService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        const val PAGE_SIZE = 50
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendArticleCardUploadNotification(
        articleCardEvent: ArticleCardEvent
    ) {
        var lastId: Long? = null
        do {
            val allowRecommendedContentNotifyMembers = memberService.findAllowRecommendedContentNotifyMembers(
                Optional.ofNullable(lastId)
            )

            if (allowRecommendedContentNotifyMembers.isEmpty()) break

            lastId = allowRecommendedContentNotifyMembers.last().pk

            applicationEventPublisher.publishEvent(
                MultiFcmArticleCardUploadEvent(
                    articleCardEvent.cardId,
                    articleCardEvent.content,
                    articleCardEvent.imgName?.let {
                        "https://${bucket}.s3.${region}.amazonaws.com/${S3ImgPathProperties.userCardImg}${it}"
                    },
                    allowRecommendedContentNotifyMembers
                )
            )
        }while (allowRecommendedContentNotifyMembers.size == PAGE_SIZE)
    }


}