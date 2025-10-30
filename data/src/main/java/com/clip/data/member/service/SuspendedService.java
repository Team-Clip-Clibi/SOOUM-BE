package com.clip.data.member.service;

import com.clip.data.member.entity.Suspended;
import com.clip.data.member.repository.SuspendedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuspendedService {
    private final SuspendedRepository suspendedRepository;

    public void save(Suspended suspended) {
        suspendedRepository.save(suspended);
    }

    public Optional<Suspended> findSuspensionByDeviceId(String deviceId) {
        return suspendedRepository.findByDeviceIdAndUntilBanAfter(deviceId, LocalDateTime.now());
    }

}
