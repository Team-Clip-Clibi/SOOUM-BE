package com.clip.data.member.repository;


import com.clip.data.member.entity.Member;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByDeviceId(String deviceId);

    @Modifying
    @Transactional
    @Query("update Member m set m.totalVisitorCnt = m.totalVisitorCnt + 1 where m = :profileOwnerMember")
    void incrementTotalVisitorCnt(@Param("profileOwnerMember") Member profileOwnerMember);

    @Query("select m from Member m where m.recommendedContentNotify = true " +
            "and (:lastPk is null or m.pk > :lastPk) " +
            "order by m.pk ")
    List<Member> findAllowRecommendedContentNotifyMembers(
            @Param("lastPk") Long lastMemberId,
            PageRequest pageRequest
    );
}
