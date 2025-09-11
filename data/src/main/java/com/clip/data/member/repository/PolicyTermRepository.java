package com.clip.data.member.repository;


import com.clip.data.member.entity.PolicyTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PolicyTermRepository extends JpaRepository<PolicyTerm, Long> {
    @Modifying
    @Transactional
    @Query("delete from PolicyTerm p where p.member.pk = :memberPk")
    void deletePolicyTerm(@Param("memberPk") Long memberPk);
}
