package com.clip.data.card.repository;

import com.clip.data.card.entity.CommentCard;
import com.clip.data.card.entity.CommentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentViewRepository extends JpaRepository<CommentView, Long> {

    @Query("SELECT COUNT(cv) FROM CommentView cv WHERE cv.targetComment = :commentCard")
    Long countView(CommentCard commentCard);
}