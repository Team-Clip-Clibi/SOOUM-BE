package com.clip.data.poll.entity;

import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "poll_option",
        indexes = {
                @Index(name = "IDX_POLL_OPTION_FEED_POLL", columnList = "FEED_POLL")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PollOption extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_POLL", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FeedPoll feedPoll;

    @Column(nullable = false)
    private String content;

    @Builder
    public PollOption(FeedPoll feedPoll, String content) {
        this.feedPoll = feedPoll;
        this.content = content;
    }
}
