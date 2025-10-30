package com.clip.data.card.repository;

import com.clip.data.card.entity.CommentCard;
import com.clip.data.card.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("select cl from CommentLike cl where cl.targetCard.pk = :likedCardPk and cl.likedMember.pk = :likedMemberPk")
    Optional<CommentLike> findCommentLiked(@Param("likedCardPk") Long likedCardPk, @Param("likedMemberPk") Long likedMemberPk);

    @Query("select cl from CommentLike cl where cl.targetCard.pk in :targetCardPks and cl.isDeleted = false")
    List<CommentLike> findByTargetCardIn(@Param("targetCardPks") List<Long> targetCardPks);

    @Query("select cl from CommentLike cl where cl.targetCard.pk = :targetCardPk and cl.isDeleted = false")
    List<CommentLike> findByTargetCard(@Param("targetCardPk") Long targetCardPk);

    List<CommentLike> findAllByTargetCard_Pk(Long cardPk);

    @Modifying
    @Query("delete from CommentLike cl where cl.targetCard in :cards")
    void deleteByCommentCard(@Param("cards") List<CommentCard> commentCards);

    @Query("select count(cl) from CommentLike cl where cl.targetCard.pk = :targetCardPk and cl.isDeleted = false")
    Integer countByTargetCard_Pk(@Param("targetCardPk") Long targetCardPk);

    @Modifying
    @Transactional
    @Query("delete from CommentLike cl where cl.likedMember.pk = :memberPk or cl.targetCard.writer.pk = :memberPk")
    void deleteAllMemberLikes(@Param("memberPk") Long memberPk);

    @Query("select cl from CommentLike cl " +
            "where cl.targetCard.pk = :targetCardPk and cl.likedMember.pk = :likedMemberPk and cl.isDeleted = false")
    Optional<CommentLike> findExistCommentLike(@Param("targetCardPk") Long targetCardPk,
                                               @Param("likedMemberPk") Long likedMemberPk);
}