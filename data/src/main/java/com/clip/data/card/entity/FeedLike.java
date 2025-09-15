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
public class FeedLike extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "TARGET_CARD")
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedCard targetCard;

    @NotNull
    @JoinColumn(name = "LIKED_MEMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member likedMember;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Builder
    public FeedLike(FeedCard targetCard, Member likedMember) {
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
