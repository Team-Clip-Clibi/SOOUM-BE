package com.clip.data.tag.entity;


import com.clip.data.card.entity.FeedCard;
import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {
                @Index(name = "idx_feed_tag_tag_feed_card", columnList = "TAG, FEED_CARD")
        }
)
public class FeedTag extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "FEED_CARD")
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedCard feedCard;

    @NotNull
    @JoinColumn(name = "TAG")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Builder
    public FeedTag(FeedCard feedCard, Tag tag) {
        this.feedCard = feedCard;
        this.tag = tag;
    }
}
