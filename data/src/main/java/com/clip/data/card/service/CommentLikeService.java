package com.clip.data.card.service;

import com.clip.data.card.entity.CommentCard;
import com.clip.data.card.entity.CommentLike;
import com.clip.data.card.repository.CommentLikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    public Optional<CommentLike> findCommentLikedOp(Long targetFeedCardPk, Long requesterPk) {
        return commentLikeRepository.findCommentLiked(targetFeedCardPk, requesterPk);
    }

    public void save(CommentLike commentLike) {
        commentLikeRepository.save(commentLike);
    }

    public CommentLike findCommentLiked(Long likedFeedCardPk, Long likedMemberPk) {
        return commentLikeRepository.findCommentLiked(likedFeedCardPk, likedMemberPk)
                .orElseThrow(() -> new EntityNotFoundException("좋아요 이력을 찾을 수 않습니다."));
    }

    public List<CommentLike> findByTargetCardIds(List<Long> targetCardPks) {
        return commentLikeRepository.findByTargetCardIn(targetCardPks);
    }

    public List<CommentLike> findByTargetCard(Long targetCardPk) {
        return commentLikeRepository.findByTargetCard(targetCardPk);
    }

    @Transactional
    public void deleteAllFeedLikes(Long commentCardPk) {
        commentLikeRepository.deleteAllInBatch(commentLikeRepository.findAllByTargetCard_Pk(commentCardPk));
    }

    @Transactional
    public void deleteByCommentCards(List<CommentCard> comments) {
        commentLikeRepository.deleteByCommentCard(comments);
    }
      
    public int countLike(Long cardPk) {
        return commentLikeRepository.countByTargetCard_Pk(cardPk);
    }

    public boolean isLiked(Long cardPk, Long memberPk) {
        return commentLikeRepository.findExistCommentLike(cardPk, memberPk).isPresent();
    }

    @Transactional
    public void deleteAllMemberLikes(Long memberPk){
        commentLikeRepository.deleteAllMemberLikes(memberPk);
    }
}