package com.clip.data.member.entity;

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

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Suspended extends BaseEntity {
    @Id
    @Tsid
    private Long pk;

    @NotNull
    @Column(name = "DEVICE_ID", unique = true)
    private String deviceId;

    @NotNull
    @Column(name = "IS_BAN_USER")
    private boolean isBanUser;

    @NotNull
    @Column(name = "UNTIL_BAN")
    private LocalDateTime untilBan;

    @Builder
    public Suspended(String deviceId, LocalDateTime untilBan, boolean isBanUser) {
        this.deviceId = deviceId;
        this.untilBan = untilBan;
        this.isBanUser = isBanUser;
    }
}