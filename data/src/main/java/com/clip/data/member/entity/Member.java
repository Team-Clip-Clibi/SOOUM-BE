package com.clip.data.member.entity;

import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @Column(name = "DEVICE_ID", unique = true)
    private String deviceId;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "DEVICE_MODEL", nullable = false)
    private String deviceModel;

    @Column(name = "DEVICE_OS_VERSION", nullable = false)
    private String deviceOsVersion;

    @Column(name = "FIREBASE_TOKEN", columnDefinition = "VARBINARY(400)")
    private String firebaseToken;

    @NotNull
    @Column(name = "NICKNAME", columnDefinition = "VARBINARY(255) NOT NULL")
    private String nickname;

    @Column(name = "IS_ALLOW_NOTIFY")
    private boolean isAllowNotify;

    @Column(name = "BAN_COUNT")
    private int banCount;

    @NotNull
    @Column(name = "TOTAL_VISITOR_CNT")
    private long totalVisitorCnt;

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;

    @Column(name = "UNTIL_BAN")
    private LocalDateTime untilBan;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @Setter
    @Column(name = "PROFILE_IMG_NAME")
    private String profileImgName;

    @Builder
    public Member(String deviceId, DeviceType deviceType, String deviceModel, String deviceOsVersion, String firebaseToken, String nickname, String profileImgName, boolean isAllowNotify) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.deviceOsVersion = deviceOsVersion;
        this.firebaseToken = firebaseToken;
        this.nickname = nickname;
        this.isAllowNotify = isAllowNotify;
        this.banCount = 0;
        this.totalVisitorCnt = 0;
        this.deletedAt = null;
        this.untilBan = null;
        this.profileImgName = profileImgName;
        this.role = Role.USER;
    }

    public LocalDateTime ban() {
        banCount++;
        role = Role.BANNED;

        if(banCount == 1)
            untilBan = LocalDateTime.now().plusDays(1);
        else if(banCount == 2)
            untilBan = LocalDateTime.now().plusDays(7);
        else if(banCount == 3)
            untilBan = LocalDateTime.now().plusDays(14);
        else
            untilBan = LocalDateTime.now().plusDays(30);

        return untilBan;
    }

    public void unban() {
        role = Role.USER;
        untilBan = null;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImgName(String profileImgName) {
        this.profileImgName = profileImgName;
    }

    public void updateFCMToken(String fcmToken) {
        this.firebaseToken = fcmToken;
    }

    public void updateNotifyAllow(boolean isAllowNotify) {
        this.isAllowNotify = isAllowNotify;
    }

    public boolean isAllowNotify() {
        return isAllowNotify && !Objects.isNull(firebaseToken);
    }


    public Member updateDeviceSpec(DeviceType deviceType, String deviceModel, String deviceOsVersion) {
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.deviceOsVersion = deviceOsVersion;
        return this;
    }

    public Member updateDeviceInfo(String deviceId, DeviceType deviceType, String deviceModel, String deviceOsVersion) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.deviceOsVersion = deviceOsVersion;
        return this;
    }

}
