package com.clip.data.rsa.service;

import com.clip.data.rsa.entity.Rsa;
import com.clip.data.rsa.repository.RsaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RsaService {
    private final RsaRepository rsaRepository;

    public Rsa findLatestRsaKey() {
        Optional<Rsa> latestRsaKey = rsaRepository.findLatestRsaKey(LocalDateTime.now());
        return latestRsaKey.orElseGet(this::findLastRsaKey);
    }

    private Rsa findLastRsaKey() {
        Optional<Rsa> lastGeneratedRsaKey = rsaRepository.findLastGeneratedRsaKey();

        if (lastGeneratedRsaKey.isEmpty()) {
            throw new IllegalArgumentException("Rsa 테이블이 비어있습니다.");
        }

        return lastGeneratedRsaKey.get();
    }

    public Rsa save(HashMap<String, String> keyPair, LocalDateTime expiredAt) {
        Rsa rsa = Rsa.builder()
                .publicKey(keyPair.get("publicKey"))
                .privateKey(keyPair.get("privateKey"))
                .expiredAt(expiredAt)
                .build();

        return rsaRepository.save(rsa);
    }

    public void deleteExpiredRsaKey() {
        rsaRepository.deleteExpiredKey(LocalDateTime.now());
    }
}
