package com.clip.data.poll.entity;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@Table(
        name = "feed_poll",
        indexes = {
                @Index(name = "IDX_FEED_POLL_FEED_CARD", columnList = "FEED_CARD")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedPoll extends BaseEntity {

    @Id @Tsid
    private Long pk;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FEED_CARD", nullable = false, unique = true,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private FeedCard feedCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "POLL_TYPE")
    private PollType pollType;

    @Builder
    public FeedPoll(FeedCard feedCard, PollType pollType) {
        this.feedCard = Objects.requireNonNull(feedCard);
        this.pollType = pollType;
    }
}
