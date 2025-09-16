package com.clip.global.util

import com.clip.data.card.entity.Card
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.CommentLike
import com.clip.data.card.entity.FeedCard
import com.clip.data.card.entity.FeedLike

object CardUtil {
    @JvmStatic
    fun isWrittenCommentCard(
        card: Card,
        commentCardList: List<CommentCard>,
        memberPk: Long
    ): Boolean =
        commentCardList.any { it.parentCardPk == card.pk && it.writer?.pk == memberPk }

    @JvmStatic
    fun isLiked(
        feed: FeedCard,
        feedLikes: List<FeedLike>,
        memberPk: Long
    ): Boolean =
        feedLikes.any { it.targetCard?.pk == feed.pk && it.likedMember?.pk == memberPk }

    @JvmStatic
    fun isLiked(
        comment: CommentCard,
        commentLikes: List<CommentLike>,
        memberPk: Long
    ): Boolean =
        commentLikes.any { it.targetCard?.pk == comment.pk && it.likedMember?.pk == memberPk }

    @JvmStatic
    fun countLikes(
        feed: FeedCard,
        feedLikes: List<FeedLike>
    ): Int =
        feedLikes.count { it.targetCard?.pk == feed.pk }

    @JvmStatic
    fun countLikes(
        comment: CommentCard,
        commentLikes: List<CommentLike>
    ): Int =
        commentLikes.count { it.targetCard?.pk == comment.pk }

    @JvmStatic
    fun countComments(
        feed: FeedCard,
        comments: List<CommentCard>
    ): Int =
        comments.count { it.parentCardPk == feed.pk }

    @JvmStatic
    fun countComments(
        parentComment: CommentCard,
        comments: List<CommentCard>
    ): Int =
        comments.count { it.parentCardPk == parentComment.pk }
}