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

    @Column(name = "COMMENT_CARD_NOTIFY")
    private boolean commentCardNotify;

    @Column(name = "CARD_LIKE_NOTIFY")
    private boolean cardLikeNotify;

    @Column(name = "FOLLOW_USER_CARD_NOTIFY")
    private boolean followUserCardNotify;

    @Column(name = "NEW_FOLLOWER_NOTIFY")
    private boolean newFollowerNotify;

    @Column(name = "CARD_NEW_COMMENT_NOTIFY")
    private boolean cardNewCommentNotify;

    @Column(name = "RECOMMENDED_CONTENT_NOTIFY")
    private boolean recommendedContentNotify;

    @Column(name = "FAVORITE_TAG_NOTIFY")
    private boolean favoriteTagNotify;

    @Column(name = "SERVICE_UPDATE_NOTIFY")
    private boolean serviceUpdateNotify;

    @Column(name = "POLICY_VIOLATION_NOTIFY")
    private boolean policyViolationNotify;

    @Builder
    public Member(
            String deviceId, DeviceType deviceType, String deviceModel, String deviceOsVersion,
            String firebaseToken, String nickname, String profileImgName
    ) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.deviceOsVersion = deviceOsVersion;
        this.firebaseToken = firebaseToken;
        this.nickname = nickname;
        this.banCount = 0;
        this.totalVisitorCnt = 0;
        this.deletedAt = null;
        this.untilBan = null;
        this.profileImgName = profileImgName;
        this.role = Role.USER;
        this.commentCardNotify = true;
        this.cardLikeNotify = true;
        this.followUserCardNotify = true;
        this.newFollowerNotify = true;
        this.cardNewCommentNotify = true;
        this.recommendedContentNotify = true;
        this.favoriteTagNotify = true;
        this.serviceUpdateNotify = true;
        this.policyViolationNotify = true;
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

    public void updateCommentCardNotify(boolean commentCardNotify) {
        this.commentCardNotify = commentCardNotify;
    }

    public boolean isAllowCommentCardNotify() {
        return commentCardNotify && !Objects.isNull(firebaseToken);
    }

    public void updateCardLikeNotify(boolean cardLikeNotify) {
        this.cardLikeNotify = cardLikeNotify;
    }

    public boolean isAllowCardLikeNotify() {
        return cardLikeNotify && !Objects.isNull(firebaseToken);
    }

    public void updateFollowUserCardNotify(boolean followUserCardNotify) {
        this.followUserCardNotify = followUserCardNotify;
    }

    public boolean isAllowFollowUserCardNotify() {
        return followUserCardNotify && !Objects.isNull(firebaseToken);
    }

    public void updateNewFollowerNotify(boolean newFollowerNotify) {
        this.newFollowerNotify = newFollowerNotify;
    }

    public boolean isAllowNewFollowerNotify() {
        return newFollowerNotify && !Objects.isNull(firebaseToken);
    }

    public void updateCardNewCommentNotify(boolean cardNewCommentNotify) {
        this.cardNewCommentNotify = cardNewCommentNotify;
    }

    public boolean isAllowCardNewCommentNotify() {
        return cardNewCommentNotify && !Objects.isNull(firebaseToken);
    }

    public void updateRecommendedContentNotify(boolean recommendedContentNotify) {
        this.recommendedContentNotify = recommendedContentNotify;
    }

    public boolean isAllowRecommendedContentNotify() {
        return recommendedContentNotify && !Objects.isNull(firebaseToken);
    }

    public void updateFavoriteTagNotify(boolean favoriteTagNotify) {
        this.favoriteTagNotify = favoriteTagNotify;
    }

    public boolean isAllowFavoriteTagNotify() {
        return favoriteTagNotify && !Objects.isNull(firebaseToken);
    }

    public void updateServiceUpdateNotify(boolean serviceUpdateNotify) {
        this.serviceUpdateNotify = serviceUpdateNotify;
    }

    public boolean isAllowServiceUpdateNotify() {
        return serviceUpdateNotify && !Objects.isNull(firebaseToken);
    }

    public void updatePolicyViolationNotify(boolean policyViolationNotify) {
        this.policyViolationNotify = policyViolationNotify;
    }

    public boolean isAllowPolicyViolationNotify() {
        return policyViolationNotify && !Objects.isNull(firebaseToken);
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
