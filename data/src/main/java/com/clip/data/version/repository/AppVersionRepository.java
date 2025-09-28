package com.clip.data.version.repository;

import com.clip.data.member.entity.DeviceType;
import com.clip.data.version.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    @Query("select a from AppVersion a where a.deviceType = :deviceType")
    Optional<AppVersion> findByDeviceType(DeviceType deviceType);

    @Query("select a.latestVersion from AppVersion a where a.deviceType = com.clip.data.member.entity.DeviceType.IOS")
    String findIosLatestVersion();
}
