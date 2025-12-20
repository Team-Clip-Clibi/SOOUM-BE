package com.clip.data.tag.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.tag.entity.FeedTag;
import com.clip.data.tag.entity.Tag;
import com.clip.data.tag.repository.FeedTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedTagService {

    private final FeedTagRepository feedTagRepository;
    private final TagService tagService;
    private static final int MAX_PAGE_SIZE = 50;

    @Transactional
    public void deleteByFeedCardPk(Long cardPk) {
        List<FeedTag> feedTags = feedTagRepository.findAllByFeedCardPk(cardPk);

        if(!feedTags.isEmpty()) {
            tagService.decrementTagCount(
                    feedTags.stream()
                            .map(FeedTag::getTag)
                            .toList()
            );

            feedTagRepository.deleteAllInBatch(feedTags);
        }
    }

    public List<FeedTag> findTop5FeedTags(List<Long> favoriteTagPks, List<Long> blockedMemberPks) {
        return blockedMemberPks.isEmpty()
                ? feedTagRepository.findTop5FeedTagsWithoutBlock(favoriteTagPks)
                : feedTagRepository.findTop5FeedTagsWithBlock(favoriteTagPks, blockedMemberPks);
    }

    public List<FeedTag> findLoadFeedTagsIn(List<FeedTag> feedTags) {
        return feedTagRepository.findLoadFeedTagsIn(feedTags);
    }

    public void saveAll(List<FeedTag> feedTagList) {
        feedTagRepository.saveAll(feedTagList);
    }

    public List<FeedTag> saveAll(FeedCard feedCard, List<Tag> tags) {
        List<FeedTag> feedTags = tags.stream()
                .map(tag -> FeedTag.builder().feedCard(feedCard).tag(tag).build())
                .toList();
        return feedTagRepository.saveAll(feedTags);
    }

    public void deleteFeedTagByMemberPk(Long memberPk) {
        List<FeedTag> feedTags = feedTagRepository.findAllByMemberPk(memberPk);

        if(!feedTags.isEmpty()) {
            tagService.decrementTagCount(
                    feedTags.stream()
                            .map(FeedTag::getTag)
                            .toList()
            );

            feedTagRepository.deleteAllInBatch(feedTags);
        }
    }

    public List<FeedTag> findFeedCardsByTag(Long tagPk, Optional<Long> lastId, List<Long> blockMemberPks) {
        PageRequest pageRequest = PageRequest.ofSize(35);
        return blockMemberPks.isEmpty()
                ? feedTagRepository.findFeedCardsByTagWithoutBlock(tagPk, lastId.orElse(null),  pageRequest)
                : feedTagRepository.findFeedCardsByTagWithBlock(tagPk, lastId.orElse(null), blockMemberPks, pageRequest);
    }

}