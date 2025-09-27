package com.clip.api.card.mapper

import com.clip.api.card.controller.dto.CreateCommentCardRequest
import com.clip.api.card.controller.dto.CreateFeedCardRequest
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.parenttype.CardType
import com.clip.data.common.deactivatewords.DeactivateTagWords
import com.clip.data.member.entity.Member
import com.clip.global.exception.IllegalArgumentException.ParameterNotFoundException
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class CardMapper(
    private val geometryFactory: GeometryFactory = GeometryFactory()
) {
    fun toFeedCard(
        request: CreateFeedCardRequest,
        requestIp: String,
        member: Member,
    ): FeedCard =
        FeedCard(
            request.content,
            request.font,
            if (request.isDistanceShared) {
                geometryFactory.createPoint(
                    Coordinate(
                        request.longitude ?: throw ParameterNotFoundException("longitude"),
                        request.latitude ?: throw ParameterNotFoundException("longitude")
                    )
                )
            } else null,
            request.imgType,
            request.imgName,
            member,
            request.isStory,
            request.tags.isEmpty() || DeactivateTagWords.deactivateWordsList.none { request.tags.contains(it) },
            requestIp,
        )

    fun toCommentCard(
        request: CreateCommentCardRequest,
        member: Member,
        parentCardType: CardType,
        parentCardId: Long,
        masterCardId: Long,
        ip: String
    ): CommentCard =
        CommentCard(
            request.content,
            request.font,
            if (request.isDistanceShared) {
                geometryFactory.createPoint(
                    Coordinate(
                        request.longitude ?: throw ParameterNotFoundException("longitude"),
                        request.latitude ?: throw ParameterNotFoundException("longitude")
                    )
                )
            } else null,
            request.imgType,
            request.imgName,
            member,
            parentCardType,
            parentCardId,
            masterCardId,
            ip
        )
}