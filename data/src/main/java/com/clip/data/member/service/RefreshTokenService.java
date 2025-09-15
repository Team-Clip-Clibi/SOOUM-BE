package com.clip.data.member.service;

import com.clip.data.member.entity.Member;
import com.clip.data.member.entity.RefreshToken;
import com.clip.data.member.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(RefreshToken refreshToken) {
        return  refreshTokenRepository.save(refreshToken);
    }

    public void save(String token, Member member) {
        refreshTokenRepository.save(new RefreshToken(token, member));
    }

    public RefreshToken findByMember(Long memberPk) {
        return refreshTokenRepository.findById(memberPk)
                .orElseThrow(() -> new EntityNotFoundException("refreshToken을 찾을 수 없습니다."));
    }

    public String findRefreshToken(Long memberPk) {
        return refreshTokenRepository.findRefreshToken(memberPk)
                .orElseThrow(() -> new EntityNotFoundException("RefreshToken을 찾을 수 없습니다."));
    }

    public void deleteRefreshToken(Long memberPk) {
        refreshTokenRepository.deleteRefreshToken(memberPk);
    }
}
