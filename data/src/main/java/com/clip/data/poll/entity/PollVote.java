package com.clip.data.poll.entity;

import com.clip.data.common.entity.BaseEntity;
import com.clip.data.member.entity.Member;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "poll_vote",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_POLL_VOTE_OPTION_VOTER", columnNames = {"POLL_OPTION", "VOTER"})
        },
        indexes = {
                @Index(name = "IDX_VOTER", columnList = "VOTER"),
                @Index(name = "IDX_FEED_POLL", columnList = "FEED_POLL"),
                @Index(name = "IDX_POLL_OPTION", columnList = "POLL_OPTION")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PollVote extends BaseEntity {

    @Id
    @Tsid
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VOTER", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_POLL", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FeedPoll feedPoll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POLL_OPTION", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PollOption pollOption;
}
