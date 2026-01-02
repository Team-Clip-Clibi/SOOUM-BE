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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
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

        // 1. 입력 정제 (순서 유지)
        List<String> normalizedTags = tags.stream()
                .map(String::strip)
                .filter(tag -> !tag.isBlank())
                .toList();

        // 2. 중복 제거 (DB/생성용)
        Set<String> uniqueContents = new LinkedHashSet<>(normalizedTags);

        // 3. 기존 태그 조회
        List<Tag> existsTags = findTagList(new ArrayList<>(uniqueContents));

        // 4. 카운트 증가 (정책적으로 1회)
        incrementTagCount(existsTags);

        Set<String> existingContents = existsTags.stream()
                .map(Tag::getContent)
                .collect(Collectors.toSet());

        // 5. 신규 태그 생성 (중복 없음)
        List<Tag> newTagList = saveAll(
                uniqueContents.stream()
                        .filter(content -> !existingContents.contains(content))
                        .map(content -> Tag.ofFeed(content, isActiveWords(content)))
                        .toList()
        );

        // 6. content → Tag 매핑
        Map<String, Tag> tagMap =
                Stream.concat(existsTags.stream(), newTagList.stream())
                        .collect(Collectors.toMap(
                                Tag::getContent,
                                Function.identity()
                        ));

        // 7. 입력 순서 기준 결과 생성
        return normalizedTags.stream()
                .map(tagMap::get)
                .filter(Objects::nonNull)
                .toList();
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
