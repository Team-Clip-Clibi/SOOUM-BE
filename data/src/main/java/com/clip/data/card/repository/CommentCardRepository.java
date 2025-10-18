package com.clip.data.card.repository;

import com.clip.data.card.entity.CommentCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentCardRepository extends JpaRepository<CommentCard, Long> {

    List<CommentCard> findAllByMasterCard(Long masterCardPk);

    @Query("select cc from CommentCard cc where cc.masterCard in :targetPks")
    List<CommentCard> findCommentCardsIn(@Param("targetPks") List<Long> targetPks);

    @Query("select cc from CommentCard cc where cc.parentCardPk = :parentCardPk")
    List<CommentCard> findChildCards(@Param("parentCardPk") Long parentCardPk);

    @Query("select cc from CommentCard cc where cc.parentCardPk in :parentCardPk")
    List<CommentCard> findChildCards(@Param("parentCardPk") List<Long> parentCardPk);

    @Query("select cc from CommentCard cc join fetch cc.writer where cc.pk = :commentCardPk")
    Optional<CommentCard> findCommentCard(@Param("commentCardPk") Long commentCardPk);

    @Query("select count(cc) from CommentCard cc where cc.isDeleted = false and cc.parentCardPk = :parentCardPk")
    Integer countCommentsByParentCard(@Param("parentCardPk") Long parentCardPk);

    @Query("select cc from CommentCard cc" +
           " join fetch cc.writer"+
            " where (:lastPk is null or cc.pk < :lastPk)" +
                " and cc.isDeleted = false" +
                " and cc.parentCardPk = :parentCardPk" +
                " and cc.writer.pk not in :blockMemberPks " +
            " order by cc.pk desc ")
    List<CommentCard> findCommentsInfo(@Param("parentCardPk") Long parentCardPk,
                                       @Param("lastPk") Long lastPk,
                                       @Param("blockMemberPks") List<Long> blockMemberPks,
                                       Pageable pageable);

    @Query("select cc from CommentCard cc where cc.writer.pk = :memberPk" +
            " and (:lastPk is null or cc.pk < :lastPk)" +
            " and cc.isDeleted = false order by cc.pk desc")
    List<CommentCard> findCommentCards(@Param("memberPk") Long memberPk, @Param("lastPk") Long lastPk, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from CommentCard cc WHERE cc.writer.pk = :memberPk")
    void deleteCommentCardByMemberPk(@Param("memberPk") Long memberPk);

}
