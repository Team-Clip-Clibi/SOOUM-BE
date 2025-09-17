package com.clip.data.member.service;

import com.clip.data.member.entity.Blacklist;
import com.clip.data.member.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlacklistService {
    private final BlacklistRepository blacklistRepository;

    public Optional<Blacklist> findByToken(String token) {
        return blacklistRepository.findById(token);
    }
    public void save(Blacklist blacklist) {
        blacklistRepository.save(blacklist);
    }
    public void saveAll (List<Blacklist> blacklists) {
        blacklistRepository.saveAll(blacklists);
    }
}
