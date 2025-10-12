package com.clip.data.card.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.repository.FeedCardRepository;
import com.clip.data.card.repository.projection.DistanceFeedCardDto;
import com.clip.data.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedCardService {
    private final FeedCardRepository feedCardRepository;
    private static final int MAX_PAGE_SIZE = 50;

    public List<FeedCard> getLatestFeeds(Optional<Long> lastId, List<Long> blockMemberPkList) {
        Pageable pageRequest = PageRequest.ofSize(MAX_PAGE_SIZE);
        return feedCardRepository.findByNextPage(lastId.orElse(null), blockMemberPkList, pageRequest);
    }

    public List<DistanceFeedCardDto> findFeedsByDistance (Optional<Long> lastPk, Point userLocation, double distance, List<Long> blockMemberPks) {
        Pageable pageRequest = PageRequest.ofSize(MAX_PAGE_SIZE);
        List<Long> queryBlockList = blockMemberPks == null || blockMemberPks.isEmpty() ? null : blockMemberPks;
        return feedCardRepository.findNextByDistance(lastPk.orElse(null), userLocation, distance, queryBlockList, pageRequest);
    }

    public void deleteFeedCard(Long feedCardPk) {
        feedCardRepository.deleteById(feedCardPk);
    }

    public FeedCard findFeedCard(Long feedCardPk) {
        return feedCardRepository.findWithMember(feedCardPk)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));
    }

    public FeedCard findFeedCardOrNull(Long commentCardPk) {
        return feedCardRepository.findById(commentCardPk).orElse(null);
    }

    public boolean isExistFeedCard(Long feedCardPk) {
        return feedCardRepository.existsById(feedCardPk);
    }

    public FeedCard findByPk(Long feedCardPk) {
        return feedCardRepository.findById(feedCardPk)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));
    }

    public FeedCard saveFeedCard(FeedCard feedCard) {
        return feedCardRepository.save(feedCard);
    }

    public Long findFeedCardCnt(Member cardOwnerMember) {
        return feedCardRepository.findFeedCardCnt(cardOwnerMember);
    }

    public List<FeedCard> findMemberFeedCards(Long memberPk, Long lastPk) {
        PageRequest pageRequest = PageRequest.ofSize(MAX_PAGE_SIZE);
        return feedCardRepository.findMemberFeedCards(memberPk, lastPk, pageRequest);
    }

    public List<FeedCard> findMyFeedCards(Long memberPk, Long lastPk) {
        PageRequest pageRequest = PageRequest.ofSize(MAX_PAGE_SIZE);
        return feedCardRepository.findMyFeedCards(memberPk, lastPk, pageRequest);
    }

    public void deleteFeedCardByMemberPk(Long memberPk) {
        feedCardRepository.deleteFeedCardByMemberPk(memberPk);
    }

    public void increaseViewCnt(Long feedCardPk, Long viewerMemberPk) {
        feedCardRepository.increaseViewCnt(feedCardPk, viewerMemberPk);
    }

}
