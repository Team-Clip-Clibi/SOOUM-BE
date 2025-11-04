package com.clip.data.member.service;

import com.clip.data.member.entity.MemberWithdrawalReason;
import com.clip.data.member.repository.MemberWithdrawalReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWithdrawalReasonService {

    private final MemberWithdrawalReasonRepository memberWithdrawalReasonRepository;

    public void save(MemberWithdrawalReason memberWithdrawalReason) {
        memberWithdrawalReasonRepository.save(memberWithdrawalReason);
    }
}
