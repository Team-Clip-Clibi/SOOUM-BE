package com.clip.data.tag.service;

import com.clip.data.tag.entity.FeedTag;
import com.clip.data.tag.repository.FeedTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedTagService {

    private final FeedTagRepository feedTagRepository;
    private final TagService tagService;

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

    public void deleteFeedTag(Long memberPk) {
        feedTagRepository.deleteFeedTag(memberPk);
    }
}