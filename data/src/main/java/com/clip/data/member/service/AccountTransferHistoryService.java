package com.clip.data.member.service;

import com.clip.data.member.entity.AccountTransferHistory;
import com.clip.data.member.repository.AccountTransferHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountTransferHistoryService {
    private final AccountTransferHistoryRepository accountTransferHistoryRepository;

    public void save(AccountTransferHistory accountTransferHistory) {
        accountTransferHistoryRepository.save(accountTransferHistory);
    }

    public Optional<AccountTransferHistory> findLatestByMemberPk(Long memberPk) {
        return accountTransferHistoryRepository.findLatestByMemberPk(memberPk);
    }
}
