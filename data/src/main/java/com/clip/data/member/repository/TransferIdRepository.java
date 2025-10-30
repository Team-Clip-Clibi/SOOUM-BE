package com.clip.data.member.repository;


import com.clip.data.member.entity.TransferId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransferIdRepository extends JpaRepository<TransferId, Long> {
    @Query(value = "select ti.transfer_id from transfer_id ti order by rand() limit 1", nativeQuery = true)
    String findRandomTransferId();
}
