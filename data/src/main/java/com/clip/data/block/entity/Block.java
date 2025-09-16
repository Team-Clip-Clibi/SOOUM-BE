package com.clip.data.block.entity;

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
public class Block extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "FROM_MEMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member fromMember;

    @NotNull
    @JoinColumn(name = "TO_MEMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member toMember;

    @Builder
    public Block(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}
