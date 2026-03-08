package com.clip.data.card.entity;

import com.clip.data.common.entity.BaseEntity;
import com.clip.data.member.entity.Member;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "feed_card_view_history",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_feed_card_view_history_viewer_target",
                        columnNames = {"viewer", "target_feed_card"}
                )
        },
        indexes = {
                @Index(name = "idx_feed_card_view_history_target_feed_card", columnList = "target_feed_card"),
                @Index(name = "idx_feed_card_view_history_viewer", columnList = "viewer")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCardViewHistory extends BaseEntity {
    @Id
    @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "TARGET_FEED_CARD", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedCard targetFeedCard;

    @NotNull
    @JoinColumn(name = "VIEWER", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member viewer;

    public FeedCardViewHistory(FeedCard targetFeedCard, Member viewer) {
        this.targetFeedCard = targetFeedCard;
        this.viewer = viewer;
    }
}
