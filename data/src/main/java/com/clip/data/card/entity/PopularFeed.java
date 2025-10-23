package com.clip.data.card.entity;

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
public class PopularFeed extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "POPULAR_CARD")
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedCard popularCard;

    @Builder
    public PopularFeed(FeedCard popularCard) {
        this.popularCard = popularCard;
    }
}
