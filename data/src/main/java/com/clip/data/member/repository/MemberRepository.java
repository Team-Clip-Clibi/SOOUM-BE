package com.clip.data.member.repository;


import com.clip.data.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByDeviceId(String deviceId);

}
