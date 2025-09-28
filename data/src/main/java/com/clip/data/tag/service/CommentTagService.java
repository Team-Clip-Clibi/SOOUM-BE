package com.clip.data.tag.service;

import com.clip.data.card.entity.CommentCard;
import com.clip.data.tag.entity.CommentTag;
import com.clip.data.tag.entity.Tag;
import com.clip.data.tag.repository.CommentTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentTagService {

    private final CommentTagRepository commentTagRepository;

    public void deleteByCommentCardPk(Long cardPk) {
        List<CommentTag> tags = commentTagRepository.findAllByCommentCardPk(cardPk);

        if(!tags.isEmpty()) {
            commentTagRepository.deleteAllInBatch(tags);
        }
    }

    public void deleteByCommentCards(List<CommentCard> commentCards) {
        List<CommentTag> tags = commentTagRepository.findAllByCommentCards(commentCards);

        if(!tags.isEmpty()) {
            commentTagRepository.deleteAllInBatch(tags);
        }
    }

    public void saveAll(List<CommentTag> commentTagList) {
        commentTagRepository.saveAll(commentTagList);
    }

    public List<CommentTag> saveAll(CommentCard commentCard, List<Tag> tags) {
        List<CommentTag> commentTags = tags.stream()
                .map(tag -> CommentTag.builder().commentCard(commentCard).tag(tag).build())
                .toList();
        return commentTagRepository.saveAll(commentTags);
    }

    public void deleteCommentTag(Long memberPk) {
        commentTagRepository.deleteCommentTag(memberPk);
    }
}
