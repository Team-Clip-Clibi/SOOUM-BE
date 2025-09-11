package com.clip.data.version.service;

import com.clip.data.version.entity.AppVersion;
import com.clip.data.version.entity.DeviceType;
import com.clip.data.version.repository.AppVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppVersionService {
    private final AppVersionRepository appVersionRepository;

    public AppVersion getAppVersionByDeviceType(DeviceType deviceType) {
        return appVersionRepository.findByDeviceType(deviceType).orElseThrow(() -> new IllegalArgumentException("AppVersion not found for device type: " + deviceType));
    }


}
