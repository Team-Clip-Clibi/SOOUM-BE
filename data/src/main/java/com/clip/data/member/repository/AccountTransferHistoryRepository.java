package com.clip.data.member.repository;

import com.clip.data.member.entity.AccountTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTransferHistoryRepository extends JpaRepository<AccountTransferHistory, Long> {

    Optional<AccountTransferHistory> findByMemberPk(Long memberPk);
}