package com.clip.data.member.repository;

import com.clip.data.member.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, String> {
}
