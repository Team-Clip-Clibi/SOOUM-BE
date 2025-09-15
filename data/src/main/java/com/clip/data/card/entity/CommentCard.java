package com.clip.data.card.entity;

import com.clip.data.card.entity.font.Font;
import com.clip.data.card.entity.fontsize.FontSize;
import com.clip.data.card.entity.imgtype.CardImgType;
import com.clip.data.card.entity.parenttype.CardType;
import com.clip.data.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCard extends Card {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "PARENT_CARD_TYPE")
    private CardType parentCardType;

    @Column(name = "PARENT_CARD_PK")
    private Long parentCardPk;

    @NotNull
    @Column(name = "MASTER_CARD_PK")
    private Long masterCard;

    @Builder
    public CommentCard(String content, FontSize fontSize, Font font, Point location, CardImgType imgType, String imgName, Member writer, CardType parentCardType, Long parentCardPk, Long masterCard, String writerIp) {
        super(content, fontSize, font, location, imgType, imgName, writer, writerIp);
        this.parentCardType = parentCardType;
        this.parentCardPk = parentCardPk;
        this.masterCard = masterCard;
    }
}
