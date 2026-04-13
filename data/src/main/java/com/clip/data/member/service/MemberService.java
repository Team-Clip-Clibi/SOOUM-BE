package com.clip.data.member.service;


import com.clip.data.member.entity.Member;
import com.clip.data.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member save(Member member) {
        memberRepository.save(member);
        return member;
    }

    public List<Member> findAllowRecommendedContentNotifyMembers(Optional<Long> lastMemberPk) {
        return memberRepository.findAllowRecommendedContentNotifyMembers(
                lastMemberPk.orElse(null),
                PageRequest.ofSize(50)
        );
    }

    public Optional<Member> findMemberOp(String deviceId) {
        return memberRepository.findByDeviceId(deviceId);
    }

    public Member findByDeviceId(String deviceId) {
        return memberRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public Member findMember(final Long memberPk) {
        return memberRepository.findById(memberPk)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public Long findTotalVisitCnt(Member member) {
        return member.getTotalVisitorCnt();
    }

    public void incrementTotalVisitorCnt(Member profileOwnerMember) {
        memberRepository.incrementTotalVisitorCnt(profileOwnerMember);
    }

    @Transactional
    public void clearFcmTokenByToken(String fcmToken) {
        memberRepository.updateFcmTokenToNullByUnregisteredToken(fcmToken);
    }
}
