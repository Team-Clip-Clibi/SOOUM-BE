package com.clip.data.tag.entity;


import com.clip.data.common.entity.BaseEntity;
import com.clip.data.member.entity.Member;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "FavoriteTag", uniqueConstraints = {
        @UniqueConstraint(name = "uc_favoritetag", columnNames = {"MEMBER", "TAG"})
})
@NoArgsConstructor
public class FavoriteTag extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "MEMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    @JoinColumn(name = "TAG")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Builder
    public FavoriteTag(Member member, Tag tag) {
        this.member = member;
        this.tag = tag;
    }
}
