package com.clip.data.card.service;

import com.clip.data.card.entity.CommentCard;
import com.clip.data.card.entity.CommentView;
import com.clip.data.card.repository.CommentViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentViewService {
    private final CommentViewRepository commentViewRepository;

    public void save(CommentView commentView) {
        commentViewRepository.save(commentView);
    }

    public Long countView(CommentCard commentCard) {
        return commentViewRepository.countView(commentCard);
    }
}
