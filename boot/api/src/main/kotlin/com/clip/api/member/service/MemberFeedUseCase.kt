package com.clip.api.member.service

import com.clip.api.member.controller.dto.CardContent
import com.clip.api.member.controller.dto.CardContentsResponse
import com.clip.data.card.entity.imgtype.CardImgType
import com.clip.data.card.service.CommentCardService
import com.clip.data.card.service.FeedCardService
import com.clip.infra.s3.S3ImgService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberFeedUseCase(
    private val feedCardService: FeedCardService,
    private val commentCardService: CommentCardService,
    private val s3ImgService: S3ImgService,
) {

    @Transactional(readOnly = true)
    fun getUserFeedCards(lastId: Long?, userId: Long): CardContentsResponse =
        feedCardService.findFeedCardsByUser(userId, lastId)
            .map { card ->
                CardContent(
                    card.pk,
                    card.imgName,
                    if (card.imgType.equals(CardImgType.USER)) {
                        s3ImgService.generateUserCardImgUrl(card.imgName)
                    } else {
                        s3ImgService.generateDefaultCardImgUrl(card.imgName)
                    },
                    card.content
                )
            }.toList().let { CardContentsResponse(it) }


    @Transactional(readOnly = true)
    fun getMyCommentCards(lastId: Long?, userId: Long): CardContentsResponse =
        commentCardService.findCommentCardsByUser(userId, lastId)
            .map { card ->
                CardContent(
                    card.pk,
                    card.imgName,
                    if (card.imgType.equals(CardImgType.USER)) {
                        s3ImgService.generateUserCardImgUrl(card.imgName)
                    } else {
                        s3ImgService.generateDefaultCardImgUrl(card.imgName)
                    },
                    card.content
                )
            }.toList().let { CardContentsResponse(it) }

}