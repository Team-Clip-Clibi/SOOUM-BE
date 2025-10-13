package com.clip.data.visitor.service;

import com.clip.data.member.entity.Member;
import com.clip.data.visitor.entity.Visitor;
import com.clip.data.visitor.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    public Optional<Visitor> saveVisitorIfFirstVisitToday(Member profileOwnerMember, Member visitorMember){

        if (isNewVisitor(profileOwnerMember, visitorMember)) {
            return Optional.of(visitorRepository.save(Visitor.builder()
                    .profileOwner(profileOwnerMember)
                    .visitor(visitorMember)
                    .build()));
        }
        return Optional.empty();
    }

    private boolean isNewVisitor(Member profileOwner, Member visitorMember) {
        return visitorRepository.findCurrentDateVisitor(profileOwner, visitorMember).isEmpty();
    }

    public Long findCurrentDateVisitorCnt(Member profileOwnerMember){
        return visitorRepository.findCurrentDateVisitorCnt(profileOwnerMember);
    }

    public void handleVisitorOnMemberWithdraw(Long memberPk) {
        visitorRepository.deleteByProfileOwner(memberPk);
        visitorRepository.updateVisitorToNull(memberPk);
    }
}
