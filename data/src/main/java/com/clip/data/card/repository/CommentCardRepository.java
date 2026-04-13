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

    @Query("select cc from CommentCard cc where cc.masterCard = :masterCardPk and cc.isDeleted = false")
    List<CommentCard> findAllByMasterCard(@Param("masterCardPk") Long masterCardPk);

    @Query("select cc from CommentCard cc where cc.masterCard in :targetPks and cc.isDeleted = false")
    List<CommentCard> findCommentCardsIn(@Param("targetPks") List<Long> targetPks);

    @Query("select cc from CommentCard cc where cc.parentCardPk = :parentCardPk and cc.isDeleted = false")
    List<CommentCard> findChildCards(@Param("parentCardPk") Long parentCardPk);

    @Query("select cc from CommentCard cc where cc.parentCardPk in :parentCardPk and cc.isDeleted = false")
    List<CommentCard> findChildCards(@Param("parentCardPk") List<Long> parentCardPk);

    @Query("select cc from CommentCard cc join fetch cc.writer where cc.parentCardPk = :parentCardPk and cc.isDeleted = false")
    List<CommentCard> findChildCardsWithWriter(@Param("parentCardPk") Long parentCardPk);

    @Query("select cc from CommentCard cc join fetch cc.writer where cc.pk = :commentCardPk and cc.isDeleted = false")
    Optional<CommentCard> findCommentCard(@Param("commentCardPk") Long commentCardPk);

    Optional<CommentCard> findByPkAndIsDeletedFalse(Long commentCardPk);

    boolean existsByPkAndIsDeletedFalse(Long commentCardPk);

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

    @Modifying
    @Transactional
    @Query("update CommentCard cc set cc.isDeleted = true where cc.pk = :commentCardPk")
    void softDeleteById(Long commentCardPk);
}
