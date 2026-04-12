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
        name = "feed_card_comment_viewer_notify_history",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_feed_card_comment_notify_history_target_recipient_writer",
                        columnNames = {"target_feed_card", "comment_writer", "recipient"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCardCommentViewerNotifyHistory extends BaseEntity {
    @Id
    @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "TARGET_FEED_CARD", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedCard targetFeedCard;

    @NotNull
    @JoinColumn(name = "RECIPIENT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member recipient;

    @NotNull
    @JoinColumn(name = "COMMENT_WRITER", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member commentWriter;

    public FeedCardCommentViewerNotifyHistory(FeedCard targetFeedCard, Member recipient, Member commentWriter) {
        this.targetFeedCard = targetFeedCard;
        this.recipient = recipient;
        this.commentWriter = commentWriter;
    }
}
