package com.clip.data.member.service;

import com.clip.data.member.entity.AccountTransfer;
import com.clip.data.member.repository.AccountTransferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountTransferService {
    private final MemberService memberService;
    private final AccountTransferRepository accountTransferRepository;
    private final TransferIdService transferIdService;

    public Optional<AccountTransfer> getByMemberPk(Long memberPk) {
        return accountTransferRepository.findByMember_Pk(memberPk);
    }

    @Transactional
    public AccountTransfer saveAccountTransfer(Long memberPk) {
        return accountTransferRepository.save(AccountTransfer.builder()
                .member(memberService.findMember(memberPk))
                .transferId(createTransferId())
                .build());
    }

    public AccountTransfer findAccountTransfer(Long memberPk) {
        return accountTransferRepository.findByMember_Pk(memberPk)
                .orElseThrow(() -> new EntityNotFoundException("계정 이관 코드를 찾을 수 없습니다."));
    }

    public AccountTransfer findAvailableAccountTransfer(String transferId) {
        return accountTransferRepository.findAvailableAccountTransfer(transferId)
                .orElseThrow(() -> new EntityNotFoundException("사용 가능한 계정 이관 코드를 찾을 수 없습니다."));
    }

    public String createTransferId() {
        for (int i = 0; i < 10; i++) {
            String id = transferIdService.findTransferId() + (100 + new Random().nextInt(900));
            if (!isValidTransferIdExists(id)) return id;
        }
        throw new IllegalStateException("고유한 transferId 생성 실패");
    }

    public boolean isValidTransferIdExists(String transferId) {
        return accountTransferRepository.existsByTransferIdAndExpirationDateAfter(transferId, LocalDateTime.now());
    }


    public void deleteAccountTransfer(Long memberPk) {
        accountTransferRepository.deleteAccountTransfer(memberPk);
    }
}
