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
    void deleteFollower(@Param("fromMember") Member fromMember, @Param("toMember") Member toMember);

    @Query("select count(f) from Follow f where f.toMember = :profileOwner")
    Long findFollowerCnt(@Param("profileOwner") Member profileOwner);

    @Query("select count(f) from Follow f where f.fromMember = :profileOwner")
    Long findFollowingCnt(@Param("profileOwner") Member profileOwner);

    @Query("select f.fromMember from Follow f where f.toMember.pk = :requesterPk and f.fromMember.pk not in :blockMembers " +
            "order by f.pk desc ")
    List<Member> findFollowers(@Param("requesterPk") Long requesterPk,
                               @Param("blockMembers") List<Long> blockMembers,
                               Pageable pageable);

    @Query("select f.fromMember from Follow f where f.toMember.pk = :requesterPk and f.fromMember.pk not in :blockMembers " +
            "and f.pk < (select fr.pk from Follow fr where fr.fromMember.pk = :followerLastPk) " +
            "order by f.pk desc ")
    List<Member> findFollowersByFollowerLastPk(@Param("followerLastPk") Long followerLastPk,
                                               @Param("requesterPk") Long requesterPk,
                                               @Param("blockMembers") List<Long> blockMembers,
                                               Pageable pageable);

    @Query("select f.toMember from Follow f where f.fromMember.pk = :requesterPk and f.toMember.pk not in :blockMembers " +
            "order by f.pk desc ")
    List<Member> findFollowings(@Param("requesterPk") Long requesterPk,
                                @Param("blockMembers") List<Long> blockMembers,
                                Pageable pageable);

    @Query("select f.toMember from Follow f where f.fromMember.pk = :requesterPk and f.toMember.pk not in :blockMembers " +
            "and f.pk < (select fr.pk from Follow fr where fr.toMember.pk = :followingLastPk) " +
            "order by f.pk desc ")
    List<Member> findFollowingsByFollowingLastPk(@Param("followingLastPk") Long followingLastPk,
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
