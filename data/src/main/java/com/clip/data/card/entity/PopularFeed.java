package com.clip.data.card.entity;

import com.clip.data.card.entity.popularitytype.PopularityType;
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

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private PopularityType popularityType;

    @Column(name = "version")
    private int version;

    @Builder
    public PopularFeed(FeedCard popularCard, PopularityType popularityType, int version) {
        this.popularCard = popularCard;
        this.popularityType = popularityType;
        this.version = version;
    }
}
