package com.clip.data.member.repository;

import com.clip.data.member.entity.InternalTester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternalTesterRepository extends JpaRepository<InternalTester, Long> {
    boolean existsByMember_Pk(Long memberPk);
}
