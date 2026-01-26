package com.clip.data.card.entity;

import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCard extends BaseEntity {
    @Id
    @Tsid
    private Long pk;

    @NotNull
    @Column(name = "FEED_CARD_PK")
    private Long feedCardPk;

    @Builder
    public ArticleCard(Long feedCardPk) {
        this.feedCardPk = feedCardPk;
    }
}

