package com.clip.data.member.repository;

import com.clip.data.member.entity.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Long> {
    boolean existsByTransferIdAndExpirationDateAfter(String transferId, LocalDateTime expirationDate);

    Optional<AccountTransfer> findByMember_Pk(Long memberPk);
    @Query("select at from AccountTransfer at join fetch at.member where at.transferId = :transferId and at.expirationDate > current_timestamp")
    Optional<AccountTransfer> findAvailableAccountTransfer(@Param("transferId") String transferId);

    @Modifying
    @Transactional
    @Query("delete from AccountTransfer at where at.member.pk = :memberPk")
    void deleteAccountTransfer(@Param("memberPk") Long memberPk);
}
