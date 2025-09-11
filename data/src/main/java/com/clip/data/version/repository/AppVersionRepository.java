package com.clip.data.version.repository;

import com.clip.data.version.entity.AppVersion;
import com.clip.data.version.entity.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    @Query("select a from AppVersion a where a.deviceType = :deviceType")
    Optional<AppVersion> findByDeviceType(DeviceType deviceType);
}
