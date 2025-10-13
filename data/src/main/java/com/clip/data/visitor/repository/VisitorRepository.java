package com.clip.data.visitor.repository;

import com.clip.data.member.entity.Member;
import com.clip.data.visitor.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    @Query("select v " +
            "from Visitor v " +
            "where v.visitDate = current_date and v.profileOwner = :profileOwner and v.visitor = :visitor")
    Optional<Visitor> findCurrentDateVisitor(@Param("profileOwner") Member profileOwner, @Param("visitor") Member visitor);

    @Query("select count(v) " +
            "from Visitor v " +
            "where v.profileOwner = :profileOwnerMember and v.visitDate = current_date ")
    Long findCurrentDateVisitorCnt(@Param("profileOwnerMember") Member profileOwnerMember);

    @Modifying
    @Transactional
    @Query("delete from Visitor v where v.profileOwner.pk = :memberPk")
    void deleteByProfileOwner(@Param("memberPk") Long memberPk);

    @Modifying
    @Transactional
    @Query("update Visitor v set v.visitor = null where v.visitor.pk = :memberPk")
    void updateVisitorToNull(@Param("memberPk") Long memberPk);
}
