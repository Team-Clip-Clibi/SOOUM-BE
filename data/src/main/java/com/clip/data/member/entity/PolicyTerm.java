package com.clip.data.member.entity;

import com.clip.data.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PolicyTerm extends BaseEntity {
    @Id
    private Long pk;

    @NotNull
    @MapsId
    @OneToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER")
    private Member member;

    @Column(name = "IS_ALLOW_TERM_ONE")
    private boolean isAllowTermOne;

    @Column(name = "IS_ALLOW_TERM_TWO")
    private boolean isAllowTermTwo;

    @Column(name = "IS_ALLOW_TERM_THREE")
    private boolean isAllowTermThree;

    @Builder
    public PolicyTerm(Member member, boolean isAllowTermOne, boolean isAllowTermTwo, boolean isAllowTermThree) {
        this.member = member;
        this.isAllowTermOne = isAllowTermOne;
        this.isAllowTermTwo = isAllowTermTwo;
        this.isAllowTermThree = isAllowTermThree;
    }
}
