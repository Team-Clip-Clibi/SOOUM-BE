package com.clip.data.member.service;

import com.clip.data.member.repository.InternalTesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternalTesterService {
    private final InternalTesterRepository internalTesterRepository;

    @Transactional(readOnly = true)
    public boolean isTester(Long memberPk) {
        return internalTesterRepository.existsByMember_Pk(memberPk);
    }
}
