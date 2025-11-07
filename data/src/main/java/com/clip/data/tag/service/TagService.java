package com.clip.data.tag.service;

import com.clip.data.card.entity.Card;
import com.clip.data.card.entity.CommentCard;
import com.clip.data.card.entity.FeedCard;
import com.clip.data.common.deactivatewords.DeactivateTagWords;
import com.clip.data.tag.entity.CommentTag;
import com.clip.data.tag.entity.FavoriteTag;
import com.clip.data.tag.entity.FeedTag;
import com.clip.data.tag.entity.Tag;
import com.clip.data.tag.repository.CommentTagRepository;
import com.clip.data.tag.repository.FavoriteTagRepository;
import com.clip.data.tag.repository.FeedTagRepository;
import com.clip.data.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final FeedTagRepository feedTagRepository;
    private final CommentTagRepository commentTagRepository;
    private final TagRepository tagRepository;
    private final FavoriteTagRepository favoriteTagRepository;


    public List<Tag> getTagsByCard(Card card) {
        if(card instanceof FeedCard feedCard){
            return feedTagRepository.findTagsByFeedCard(feedCard).stream().map(FeedTag::getTag).toList();
        }
        if(card instanceof CommentCard commentCard){
            return commentTagRepository.findTagsByCommentCard(commentCard).stream().map(CommentTag::getTag).toList();
        }
        throw new IllegalArgumentException();
    }

    public List<Tag> findRelatedTags(String keyword, Integer size) {
        return tagRepository.findByKeyword(keyword, PageRequest.of(0, size));
    }

    public List<Tag> findTagList(List<String> tagContents) {
        return tagRepository.findTagList(tagContents);
    }

    public void incrementTagCount(List<Tag> tags){
        tagRepository.incrementTagCount(tags);
    }

    public void decrementTagCount(List<Tag> tags){
        tagRepository.decrementTagCount(tags);
    }

    @Transactional
    public List<Tag> saveAll(List<Tag> tags) {
        return tagRepository.saveAll(tags);
    }

    @Transactional
    public List<Tag> saveAllAndIncrementTagCnt(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        List<Tag> existsTags = findTagList(tags);
        incrementTagCount(existsTags);

        tags.removeAll(
                existsTags.stream()
                        .map(Tag::getContent)
                        .toList()
        );

        List<Tag> newTagList = saveAll(
                tags.stream()
                        .map(tag -> Tag.ofFeed(tag, isActiveWords(tag)))
                        .toList()
        );

        return Stream.concat(existsTags.stream(), newTagList.stream()).toList();
    }

    @Transactional
    public List<Tag> saveAllAndNoIncrementTagCnt(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        List<Tag> existsTags = findTagList(tags);

        tags.removeAll(
                existsTags.stream()
                        .map(Tag::getContent)
                        .toList()
        );

        List<Tag> newTagList = saveAll(
                tags.stream()
                        .map(tag -> Tag.ofFeed(tag, isActiveWords(tag)))
                        .toList()
        );

        return Stream.concat(existsTags.stream(), newTagList.stream()).toList();
    }

    private boolean isActiveWords(String tagContent) {
        return !DeactivateTagWords.deactivateWordsList.contains(tagContent);
    }

    public List<Tag> findRecommendTags(List<Tag> excludeTags) {
        return tagRepository.findRecommendTagList(excludeTags, DeactivateTagWords.deactivateWordsList, PageRequest.ofSize(10));
    }
    public boolean isExistFavoriteTag(Long tagPk, Long memberPk) {
        return favoriteTagRepository.existsByTag_PkAndMember_Pk(tagPk, memberPk);
    }

    @Transactional
    public void saveFavoriteTag(FavoriteTag favoriteTag) {
        favoriteTagRepository.save(favoriteTag);
    }

    public Tag findTag(Long tagPk) {
        return tagRepository.findById(tagPk)
                .orElseThrow(() -> new EntityNotFoundException("태그를 찾을 수 없습니다."));
    }

    public FavoriteTag findFavoriteTag(Long tagPk, Long memberPk) {
        return favoriteTagRepository.findByTag_PkAndMember_Pk(tagPk, memberPk)
                .orElseThrow(() -> new EntityNotFoundException("태그를 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteFavoriteTag(FavoriteTag favoriteTag) {
        favoriteTagRepository.delete(favoriteTag);
    }

    public Long findMyFavoriteTagsCount(long userId) {
        return favoriteTagRepository.findFavoriteTagCntByUserId(userId);
    }

    public List<Tag> findTop10Tags() {
        return tagRepository.findTop10Tags(DeactivateTagWords.deactivateWordsList);
    }
}
