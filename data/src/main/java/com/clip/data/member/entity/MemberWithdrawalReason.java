package com.clip.data.member.entity;

import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWithdrawalReason extends BaseEntity {

    @Id
    @Tsid
    private Long pk;

    private String reason;

    public MemberWithdrawalReason(String reason) {
        this.reason = reason;
    }

}
