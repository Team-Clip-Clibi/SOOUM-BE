package com.clip.data.notification.entity;


import com.clip.data.card.entity.Card;
import com.clip.data.card.entity.CommentCard;
import com.clip.data.card.entity.FeedCard;
import com.clip.data.common.entity.BaseEntity;
import com.clip.data.member.entity.Member;
import com.clip.data.notification.entity.notificationtype.NotificationType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationHistory extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @Column(name = "targetCardPk")
    private Long targetCardPk;

    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;

    @Column(name = "isRead")
    private boolean isRead;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime readAt;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    @JoinColumn(name = "fromMember", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member fromMember;

    @NotNull
    @JoinColumn(name = "toMember", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member toMember;

    @Builder(access = AccessLevel.PRIVATE)
    private NotificationHistory(Member fromMember, Member toMember, NotificationType notificationType, Long targetCardPk, LocalDateTime readAt) {
        this.isRead = false;
        this.targetCardPk = targetCardPk;
        this.notificationType = notificationType;
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.readAt = readAt;
    }

    public static NotificationHistory ofCommentWrite(Member fromMember, Long targetCardPk, Card parentCard) {
        return ofGeneral(fromMember, targetCardPk, parentCard, NotificationType.COMMENT_WRITE);
    }

    public static NotificationHistory ofFeedLike(Member fromMember, FeedCard targetCard) {
        return ofGeneral(fromMember, targetCard.getPk(), targetCard, NotificationType.FEED_LIKE);
    }

    public static NotificationHistory ofCommentLike(Member fromMember, CommentCard targetCard) {
        return ofGeneral(fromMember, targetCard.getPk(), targetCard, NotificationType.COMMENT_LIKE);
    }

    private static NotificationHistory ofGeneral(Member fromMember, Long targetCardPk, Card targetCard, NotificationType notificationType) {
        return NotificationHistory.builder()
                .notificationType(notificationType)
                .fromMember(fromMember)
                .toMember(targetCard.getWriter())
                .targetCardPk(targetCardPk)
                .build();
    }

    public static NotificationHistory ofBlocked(Member toMember) {
        return ofSystem(toMember, NotificationType.BLOCKED);
    }

    public static NotificationHistory ofDeleted(Member toMember) {
        return ofSystem(toMember, NotificationType.DELETED);
    }

    public static NotificationHistory ofFollow(Member fromMember, Member toMember) {
        return NotificationHistory.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .notificationType(NotificationType.FOLLOW)
                .build();
    }

    private static NotificationHistory ofSystem(Member toMember, NotificationType notificationType) {
        return NotificationHistory.builder()
                .toMember(toMember)
                .notificationType(notificationType)
                .build();
    }
}
