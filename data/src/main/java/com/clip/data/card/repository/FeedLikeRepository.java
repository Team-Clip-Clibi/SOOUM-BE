package com.clip.data.card.repository;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    @Query("select fl from FeedLike fl where fl.targetCard in :targetList and fl.isDeleted = false")
    List<FeedLike> findByTargetList(@Param("targetList") List<FeedCard> targetList);

    @Query("select fl from FeedLike fl where fl.targetCard.pk in :targetCardPks and fl.isDeleted = false")
    List<FeedLike> findByTargetCardPks(@Param("targetCardPks") List<Long> targetCardPks);

    @Modifying
    @Transactional
    @Query("delete from FeedLike fl where fl.targetCard.pk = :feedCardPk")
    void deleteAllFeedLikes(@Param("feedCardPk") Long feedCardPk);

    @Query("select fl from FeedLike fl where fl.targetCard.pk = :likedCardPk and fl.likedMember.pk = :likedMemberPk")
    Optional<FeedLike> findFeedLiked(@Param("likedCardPk") Long likedCardPk, @Param("likedMemberPk") Long likedMemberPk);

    @Query("select count(fl) from FeedLike fl where fl.targetCard.pk = :targetCardPk and fl.isDeleted = false")
    Integer countByTargetCard_Pk(@Param("targetCardPk") Long targetCardPk);

    @Modifying
    @Transactional
    @Query("delete from FeedLike fl where fl.likedMember.pk = :memberPk or fl.targetCard.writer.pk = :memberPk")
    void deleteAllMemberLikes(@Param("memberPk") Long memberPk);

    @Query("select fl from FeedLike fl " +
            "where fl.targetCard.pk = :targetCardPk and fl.likedMember.pk = :likedMemberPk and fl.isDeleted = false")
    Optional<FeedLike> findExistFeedLike(@Param("targetCardPk") Long targetCardPk,
                                         @Param("likedMemberPk") Long likedMemberPk);
}
