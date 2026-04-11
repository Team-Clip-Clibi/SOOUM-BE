package com.clip.data.card.service;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.entity.FeedCardCommentViewerNotifyHistory;
import com.clip.data.card.repository.FeedCardCommentViewerNotifyHistoryRepository;
import com.clip.data.member.entity.Member;
import com.clip.data.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedCardCommentViewerNotifyHistoryService {
    private final FeedCardCommentViewerNotifyHistoryRepository repository;
    private final FeedCardService feedCardService;
    private final MemberService memberService;

    @Transactional
    public List<Member> markFirstNotifiedViewers(Long feedCardPk, Long commentWriterPk, List<Member> viewers) {
        if (viewers == null || viewers.isEmpty() || commentWriterPk == null) {
            return Collections.emptyList();
        }

        Map<Long, Member> uniqueRecipients = toUniqueRecipients(viewers);
        if (uniqueRecipients.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> recipientPks = new ArrayList<>(uniqueRecipients.keySet());
        Set<Long> alreadyNotifiedRecipients = new HashSet<>(
                repository.findRecipientPksAlreadyNotifiedByFeedCardAndCommentWriter(
                        feedCardPk, commentWriterPk, recipientPks
                )
        );

        FeedCard feedCard = feedCardService.findByPk(feedCardPk);
        Member commentWriter = memberService.findMember(commentWriterPk);

        return uniqueRecipients.entrySet().stream()
                .filter(e -> !alreadyNotifiedRecipients.contains(e.getKey()))
                .filter(e -> saveNotifyHistory(feedCard, e.getValue(), commentWriter))
                .map(Map.Entry::getValue)
                .toList();
    }

    private Map<Long, Member> toUniqueRecipients(List<Member> viewers) {
        return viewers.stream()
                .filter(v -> v.getPk() != null)
                .collect(Collectors.toMap(
                        Member::getPk,
                        v -> v,
                        (existing, duplicate) -> existing,
                        LinkedHashMap::new
                ));
    }

    private boolean saveNotifyHistory(FeedCard feedCard, Member recipient, Member commentWriter) {
        try {
            repository.saveAndFlush(new FeedCardCommentViewerNotifyHistory(feedCard, recipient, commentWriter));
            return true;
        } catch (DataIntegrityViolationException e) {
            // Concurrent request already inserted the same (feedCard, recipient, commentWriter)
            return false;
        }
    }
}