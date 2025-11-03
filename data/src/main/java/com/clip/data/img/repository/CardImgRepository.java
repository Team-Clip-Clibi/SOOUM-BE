package com.clip.data.img.repository;

import com.clip.data.img.entity.CardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CardImgRepository extends JpaRepository<CardImg, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from CardImg u where u.imgName = :imgName")
    void deleteByImgName(@Param("imgName") String imgName);

    Optional<CardImg> findByImgName(String imgName);

    @Modifying
    @Transactional
    @Query("update CardImg ci set ci.feedCard = null where ci.feedCard.writer.pk = :memberPk")
    void updateFeedCardImgNull(@Param("memberPk") Long memberPk);

    @Modifying
    @Transactional
    @Query("update CardImg ci set ci.commentCard = null where ci.commentCard.writer.pk = :memberPk")
    void updateCommentCardImgNull(@Param("memberPk") Long memberPk);
}
