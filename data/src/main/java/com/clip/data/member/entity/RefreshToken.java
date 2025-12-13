package com.clip.data.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(name = "REFRESH_TOKEN", length = 300)
    private String refreshToken;

    @NotNull
    @OneToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "MEMBER")
    private Member member;

    @Builder
    public RefreshToken(String refreshToken, Member member) {
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public RefreshToken update(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
