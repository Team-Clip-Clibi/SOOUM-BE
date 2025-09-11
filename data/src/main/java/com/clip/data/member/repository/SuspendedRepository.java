package com.clip.data.member.repository;

import com.clip.data.member.entity.Suspended;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SuspendedRepository extends JpaRepository<Suspended, Long> {
    Optional<Suspended> findByDeviceIdAndUntilBanAfter(String deviceId, LocalDateTime now);

    @Modifying
    @Transactional
    @Query("delete from Suspended s where s.deviceId = :deviceId")
    void deleteByDeviceId(@Param("deviceId") String deviceId);
}
