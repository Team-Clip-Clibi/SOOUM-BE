package com.clip.data.follow.service;

import com.clip.data.follow.entity.Follow;
import com.clip.data.follow.repository.FollowRepository;
import com.clip.data.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    public boolean isAlreadyFollowing(Member fromMember, Member toMember) {
        return followRepository.findFollow(fromMember, toMember).isPresent();
    }

    @Transactional
    public void saveFollower(Member fromMember, Member toMember) {
        followRepository.save(Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build());
    }

    @Transactional
    public void deleteFollower(Member fromMember, Member toMember) {
        followRepository.deleteFollower(fromMember, toMember);
    }

    public Long findFollowerCnt(Member profileOwner) {
        return followRepository.findFollowerCnt(profileOwner);
    }

    public Long findFollowingCnt(Member profileOwner) {
        return followRepository.findFollowingCnt(profileOwner);
    }

    @Transactional(readOnly = true)
    public List<Member> findFollowerWithoutBlockedMembers(Optional<Long> followerLastId, Long requesterPk, List<Long> blockMembersPk) {
        PageRequest pageRequest = PageRequest.ofSize(50);
        if (followerLastId.isEmpty()) {
            return followRepository.findFollowers(requesterPk, blockMembersPk, pageRequest);
        }
        return followRepository.findFollowersByFollowerLastPk(followerLastId.get(), requesterPk, blockMembersPk, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Member> findFollowingWithoutBlockedMembers(Optional<Long> followingLastId, Long requesterPk, List<Long> blockMembersPk) {
        PageRequest pageRequest = PageRequest.ofSize(50);
        if (followingLastId.isEmpty()) {
            return followRepository.findFollowings(requesterPk, blockMembersPk, pageRequest);
        }
        return followRepository.findFollowingsByFollowingLastPk(followingLastId.get(), requesterPk, blockMembersPk, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Long> findFollowedFollowersPk(Long requesterPk, List<Member> followers) {
        return followRepository.findFollowedFollowers(requesterPk, followers);
    }

    @Transactional(readOnly = true)
    public List<Long> findFollowedFollowingsPk(Long requesterPk, List<Member> followings) {
        return followRepository.findFollowedFollowings(requesterPk, followings);
    }

    public void deleteAllFollow(Long memberPk) {
        followRepository.deleteAllFollow(memberPk);
    }
}