package com.clip.data.img.entity;

import com.clip.data.card.entity.Card;
import com.clip.data.card.entity.CommentCard;
import com.clip.data.card.entity.FeedCard;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardImg extends Img {
    @JoinColumn(name = "FEED_CARD")
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedCard feedCard;

    @JoinColumn(name = "COMMENT_CARD")
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentCard commentCard;

    @Builder
    public CardImg(String imgName, Card card) {
        super(imgName);
        if (card instanceof FeedCard feedCard) {
            this.feedCard = feedCard;
        } else if (card instanceof CommentCard commentCard) {
            this.commentCard = commentCard;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public CardImg(String imgName) {
        super(imgName);
    }

    public void updateFeedCard(FeedCard feedCard) {
        this.feedCard = feedCard;
    }

    public void updateCommentCard(CommentCard commentCard) {
        this.commentCard = commentCard;
    }
}