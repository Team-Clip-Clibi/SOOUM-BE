package com.clip.data.member.repository;

import com.clip.data.member.entity.AccountTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountTransferHistoryRepository extends JpaRepository<AccountTransferHistory, Long> {

    @Query("select ath from AccountTransferHistory ath join fetch ath.member m where m.pk = :memberPk")
    Optional<AccountTransferHistory> findByMemberPk(Long memberPk);

    @Modifying
    @Query("delete from AccountTransferHistory ath where ath.member.pk = :memberPk")
    void deleteAccountTransferHistoryByMemberPk(Long memberPk);
}