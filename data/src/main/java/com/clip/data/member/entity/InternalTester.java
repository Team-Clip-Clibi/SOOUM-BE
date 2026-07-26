package com.clip.data.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_internal_tester_member", columnNames = {"member"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InternalTester {
    @Id
    @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "MEMBER", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public InternalTester(Member member) {
        this.member = member;
    }
}
