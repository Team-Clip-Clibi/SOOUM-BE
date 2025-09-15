package com.clip.data.tag.service;

import com.clip.data.member.service.MemberService;
import com.clip.data.tag.entity.FavoriteTag;
import com.clip.data.tag.entity.Tag;
import com.clip.data.tag.repository.FavoriteTagRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteTagService {
    private final FavoriteTagRepository favoriteTagRepository;
    private final TagService tagService;
    private final MemberService memberService;

    public boolean isExistsByTagPkAndMemberPk(Long tagPk, Long memberPk) {
        return favoriteTagRepository.existsByTag_PkAndMember_Pk(tagPk, memberPk);
    }

    @Transactional
    public void saveFavoriteTag(Long tagPk, Long memberPk) {
        if (tagService.isExistFavoriteTag(tagPk, memberPk)) {
            throw new EntityExistsException("이미 즐겨찾기한 태그입니다.");
        }

        FavoriteTag favoriteTag = FavoriteTag.builder()
                .tag(tagService.findTag(tagPk))
                .member(memberService.findMember(memberPk))
                .build();
        tagService.saveFavoriteTag(favoriteTag);
    }

    public List<Tag> findFavoriteTags(Long memberPk) {
        return favoriteTagRepository.findFavoriteTag(memberPk);
    }

    @Transactional
    public void deleteFavoriteTag(Long tagPk, Long memberPk) {
        FavoriteTag findFavoriteTag = tagService.findFavoriteTag(tagPk, memberPk);
        tagService.deleteFavoriteTag(findFavoriteTag);
    }

    public List<Long> findMyFavoriteTags(Long memberPk, Optional<Long> lastTagPk, Pageable pageable) {
        return favoriteTagRepository.findMyFavoriteTags(memberPk, lastTagPk.orElse(null), pageable);
    }

    public void deleteAllFavoriteTag(Long memberPk) {
        favoriteTagRepository.deleteAllFavoriteTag(memberPk);
    }
}