package com.clip.data.card.entity;

import com.clip.data.card.entity.font.Font;
import com.clip.data.card.entity.imgtype.CardImgType;
import com.clip.data.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.Objects;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCard extends Card {
    @Column(name = "IS_STORY")
    private boolean isStory;

    @Column(name = "IS_FEED_ACTIVE")
    private boolean isFeedActive;

    @Builder
    public FeedCard(String content, Font font, Point location, CardImgType imgType, String imgName, Member writer, boolean isStory, boolean isFeedActive, String writerIp) {
        super(content, font, location, imgType, imgName, writer, writerIp);
        this.isStory = isStory;
        this.isFeedActive = isFeedActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedCard feedCard = (FeedCard) o;
        return getPk().equals(feedCard.getPk());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPk());
    }
}
