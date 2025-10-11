package com.clip.data.card.entity;

import com.clip.data.common.entity.BaseEntity;
import com.clip.data.member.entity.Member;
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
        uniqueConstraints = @UniqueConstraint(
                name = "uk_comment_like_card_member",
                columnNames = {"TARGET_CARD", "LIKED_MEMBER"}
        )
)
public class CommentLike extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "TARGET_CARD")
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentCard targetCard;

    @NotNull
    @JoinColumn(name = "LIKED_MEMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member likedMember;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Version
    private Long version;

    @Builder
    public CommentLike(CommentCard targetCard, Member likedMember) {
        this.targetCard = targetCard;
        this.likedMember = likedMember;
    }

    public void create() {
        this.isDeleted = false;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
