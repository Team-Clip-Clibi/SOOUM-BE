package com.clip.data.member.repository;

import com.clip.data.member.entity.MemberWithdrawalReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWithdrawalReasonRepository extends JpaRepository<MemberWithdrawalReason, Long> {
}