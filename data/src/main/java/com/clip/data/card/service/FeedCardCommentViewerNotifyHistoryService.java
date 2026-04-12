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

import java.util.*;
import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
//public class FeedCardCommentViewerNotifyHistoryService {
//    private final FeedCardCommentViewerNotifyHistoryRepository repository;
//    private final FeedCardService
// feedCardService;
//    private final MemberService memberService;
//
//    @Transactional
//    public List<Member> markFirstNotifiedViewers(Long feedCardPk, Long commentWriterPk, List<Member> viewers) {
//        if (viewers == null || viewers.isEmpty() || feedCardPk == null || commentWriterPk == null) {
//            return Collections.emptyList();
//        }
//
//        List<Long> recipientPks = viewers.stream()
//                .map(Member::getPk)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        Set<Long> alreadyNotifiedRecipients = new HashSet<>(
//                repository.findRecipientPksAlreadyNotifiedByFeedCardAndCommentWriter(
//                        feedCardPk, commentWriterPk, recipientPks
//                )
//        );
//
//        FeedCard feedCard = feedCardService.findByPk(feedCardPk);
//        Member commentWriter = memberService.findMember(commentWriterPk);
//
//        return recipientPks.stream()
//                .filter(pk -> !alreadyNotifiedRecipients.contains(pk))
//                .filter(pk -> saveNotifyHistory(feedCard, memberService.findMember(pk), commentWriter))
//                .map(memberService::findMember)
//                .toList();
//    }
//
//    private boolean saveNotifyHistory(FeedCard feedCard, Member recipient, Member commentWriter) {
//        try {
//            repository.saveAndFlush(new FeedCardCommentViewerNotifyHistory(feedCard, recipient, commentWriter));
//            return true;
//        } catch (DataIntegrityViolationException e) {
//            // Concurrent request already inserted the same (feedCard, recipient, commentWriter)
//            return false;
//        }
//    }
//}

@Service
@RequiredArgsConstructor
public class FeedCardCommentViewerNotifyHistoryService {
    private final FeedCardCommentViewerNotifyHistoryRepository repository;
    private final FeedCardService feedCardService;
    private final MemberService memberService;

    @Transactional
    public List<Member> markFirstNotifiedViewers(Long feedCardPk, Long commentWriterPk, List<Member> viewers) {
        if (viewers == null || viewers.isEmpty() || feedCardPk == null || commentWriterPk == null) {
            return Collections.emptyList();
        }

        List<Long> recipientPks = viewers.stream()
                .map(Member::getPk)
                .filter(Objects::nonNull)
                .toList();

        Set<Long> alreadyNotifiedRecipients = new HashSet<>(
                repository.findRecipientPksAlreadyNotifiedByFeedCardAndCommentWriter(
                        feedCardPk, commentWriterPk, recipientPks
                )
        );

        List<Long> targetsToNotify = recipientPks.stream()
                .filter(pk -> !alreadyNotifiedRecipients.contains(pk))
                .toList();

        if (!targetsToNotify.isEmpty()) {
            repository.bulkUpsertNotifyHistories(feedCardPk, commentWriterPk, targetsToNotify);
        }

        return targetsToNotify.stream()
                .map(memberService::findMember)
                .toList();
    }
}