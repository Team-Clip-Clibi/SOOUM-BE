package com.clip.data.rsa.repository;

import com.clip.data.rsa.entity.Rsa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RsaRepository extends JpaRepository<Rsa, Long> {
    @Query("select r from Rsa r where r.expiredAt > :currentDate order by r.id desc limit 1")
    Optional<Rsa> findLatestRsaKey(@Param("currentDate") LocalDateTime currentDate);

    @Query("select r from Rsa r order by r.id desc limit 1")
    Optional<Rsa> findLastGeneratedRsaKey();

    @Modifying
    @Transactional
    @Query("delete from Rsa r where r.expiredAt < :currentDate ")
    void deleteExpiredKey(@Param("currentDate")LocalDateTime currentDate);
}
