package com.clip.data.member.repository;

import com.clip.data.member.entity.AccountTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountTransferHistoryRepository extends JpaRepository<AccountTransferHistory, Long> {

    @Query("select ath from AccountTransferHistory ath where ath.member.pk = :memberPk order by ath.createdAt desc limit 1")
    Optional<AccountTransferHistory> findLatestByMemberPk(Long memberPk);
}