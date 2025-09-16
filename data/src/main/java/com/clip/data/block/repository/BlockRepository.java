package com.clip.data.block.repository;

import com.clip.data.block.entity.Block;
import com.clip.data.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query("select b.toMember.pk from Block b where b.fromMember.pk = :memberPk")
    List<Long> findAllBlockToPk(@Param("memberPk") Long memberPk);
    boolean existsByFromMemberPkAndToMemberPk(Long fromMemberPk, Long toMemberPk);

    @Modifying
    @Transactional
    @Query("delete from Block b where b.fromMember = :fromMember and b.toMember = :toMember")
    void deleteBlockMember(@Param("fromMember") Member fromMember, @Param("toMember") Member toMember);

    @Modifying
    @Transactional
    @Query("delete from Block b where b.fromMember.pk = :memberPk or b.toMember.pk = :memberPk")
    void deleteAllBlockMember(@Param("memberPk") Long memberPk);
}
