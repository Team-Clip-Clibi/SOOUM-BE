package com.clip.data.follow.repository;

import com.clip.data.follow.entity.Follow;
import com.clip.data.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("select f from Follow f where f.fromMember = :fromMember and f.toMember = :toMember")
    Optional<Follow> findFollow(@Param("fromMember") Member fromMember, @Param("toMember") Member toMember);

    @Modifying
    @Transactional
    @Query("delete from Follow f where f.fromMember = :fromMember and f.toMember = :toMember")
    void deleteFollow(@Param("fromMember") Member fromMember, @Param("toMember") Member toMember);

    @Query("select count(f) from Follow f where f.toMember = :profileOwner")
    Long findFollowerCnt(@Param("profileOwner") Member profileOwner);

    @Query("select count(f) from Follow f where f.fromMember = :profileOwner")
    Long findFollowingCnt(@Param("profileOwner") Member profileOwner);

    @Query("select f from Follow f join fetch f.fromMember where f.toMember.pk = :requesterPk and f.fromMember.pk not in :blockMembers " +
            "order by f.pk desc ")
    List<Follow> findFollowers(@Param("requesterPk") Long requesterPk,
                               @Param("blockMembers") List<Long> blockMembers,
                               Pageable pageable);

    @Query("select f from Follow f join fetch f.fromMember where f.toMember.pk = :requesterPk and f.fromMember.pk not in :blockMembers " +
            "and f.pk < :lastPk " +
            "order by f.pk desc ")
    List<Follow> findFollowersByFollowerLastPk(@Param("lastPk") Long lastPk,
                                               @Param("requesterPk") Long requesterPk,
                                               @Param("blockMembers") List<Long> blockMembers,
                                               Pageable pageable);

    @Query("select f from Follow f join fetch f.toMember where f.fromMember.pk = :requesterPk and f.toMember.pk not in :blockMembers " +
            "order by f.pk desc ")
    List<Follow> findFollowings(@Param("requesterPk") Long requesterPk,
                                @Param("blockMembers") List<Long> blockMembers,
                                Pageable pageable);

    @Query("select f from Follow f join fetch f.toMember where f.fromMember.pk = :requesterPk and f.toMember.pk not in :blockMembers " +
            "and f.pk < :lastPk " +
            "order by f.pk desc ")
    List<Follow> findFollowingsByFollowingLastPk(@Param("lastPk") Long lastPk,
                                                 @Param("requesterPk") Long requesterPk,
                                                 @Param("blockMembers") List<Long> blockMembers,
                                                 Pageable pageable);

    @Query("select f.toMember.pk from Follow f where f.fromMember.pk = :requesterPk and f.toMember in :followers")
    List<Long> findFollowedFollowers(@Param("requesterPk") Long requesterPk, @Param("followers") List<Member> followers);

    @Query("select f.toMember.pk from Follow f where f.fromMember.pk = :requesterPk and f.toMember in :followings")
    List<Long> findFollowedFollowings(@Param("requesterPk") Long requesterPk, @Param("followings") List<Member> followings);

    @Modifying
    @Transactional
    @Query("delete from Follow f where f.fromMember.pk = :memberPk or f.toMember.pk = :memberPk")
    void deleteAllFollow(@Param("memberPk") Long memberPk);
}