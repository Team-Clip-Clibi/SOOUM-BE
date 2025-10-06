package com.clip.data.card.entity;

import com.clip.data.card.entity.font.Font;
import com.clip.data.card.entity.imgtype.CardImgType;
import com.clip.data.common.entity.BaseEntity;
import com.clip.data.member.entity.Member;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Card extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Font font;

    @Column(name = "LOCATION", columnDefinition = "GEOMETRY")
    private Point location;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private CardImgType imgType;

    @Column(name = "IMG_NAME")
    private String imgName;

    @Setter(AccessLevel.PUBLIC)
    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @JoinColumn(name = "WRITER", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @Column(name = "VIEW_CNT")
    private int viewCnt = 0;

    @NotNull
    @Column(name = "WRITER_IP")
    private String writerIp;

    public void changeDeleteStatus() {
        this.isDeleted = true;
    }

    public Card(String content, Font font, Point location, CardImgType imgType, String imgName, Member writer, String writerIp) {
        this.content = content;
        this.font = font;
        this.location = location;
        this.imgType = imgType;
        this.imgName = imgName;
        this.writer = writer;
        this.writerIp = writerIp;
    }

    public boolean isWriter(Long compareMemberPk) {
        return this.writer.getPk().equals(compareMemberPk);
    }
}
