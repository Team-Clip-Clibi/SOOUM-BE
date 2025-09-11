package com.clip.data.member.service;

import com.clip.data.member.entity.PolicyTerm;
import com.clip.data.member.repository.PolicyTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyTermRepository policyRepository;

    public PolicyTerm save(PolicyTerm policyTerm) {
        return policyRepository.save(policyTerm);
    }

    public void deletePolicyTerm(Long memberPk) {
        policyRepository.deletePolicyTerm(memberPk);
    }
}
