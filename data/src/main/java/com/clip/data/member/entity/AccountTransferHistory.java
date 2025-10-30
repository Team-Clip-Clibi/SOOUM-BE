package com.clip.data.member.entity;

import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountTransferHistory extends BaseEntity {
    @Id
    @Tsid
    private Long pk;

    @NotNull
    @JoinColumn(name = "MEMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    @Column(name = "TRANSFER_AT")
    private LocalDateTime transferAt;

    @Builder
    public AccountTransferHistory(Member member) {
        this.member = member;
        this.transferAt = LocalDateTime.now();
    }

    public void updateTransferAt() {
        this.transferAt = LocalDateTime.now();
    }
}
