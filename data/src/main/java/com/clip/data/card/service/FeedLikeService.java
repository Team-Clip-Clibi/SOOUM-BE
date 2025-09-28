package com.clip.data.card.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.FeedLike;
import com.clip.data.card.repository.FeedLikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;

    public List<FeedLike> findByTargetCards(List<FeedCard> targetCards) {
        return feedLikeRepository.findByTargetList(targetCards);
    }

    public List<FeedLike> findByTargetCardIds(List<Long> targetCardPks) {
        return feedLikeRepository.findByTargetCardPks(targetCardPks);
    }

    public void deleteAllFeedLikes(Long feedCardPk) {
        feedLikeRepository.deleteAllFeedLikes(feedCardPk);
    }

    public int countLike(Long feedCardPk) {
        return feedLikeRepository.countByTargetCard_Pk(feedCardPk);
    }

    public void save(FeedLike feedLike) {
        feedLikeRepository.save(feedLike);
    }

    public Optional<FeedLike> findFeedLikedOp(Long targetFeedCardPk, Long requesterPk) {
        return feedLikeRepository.findFeedLiked(targetFeedCardPk, requesterPk);
    }

    public FeedLike findFeedLiked(Long likedFeedCardPk, Long likedMemberPk) {
        return feedLikeRepository.findFeedLiked(likedFeedCardPk, likedMemberPk)
                .orElseThrow(() -> new EntityNotFoundException("좋아요 이력을 찾을 수 않습니다."));
    }

    public boolean isLiked(Long cardPk, Long memberPk){
        return feedLikeRepository.findExistFeedLike(cardPk, memberPk).isPresent();
    }

    public void deleteAllMemberLikes(Long memberPk){
        feedLikeRepository.deleteAllMemberLikes(memberPk);
    }
}
