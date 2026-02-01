package com.clip.data.abtest.service;

import com.clip.data.abtest.entity.AbGroup;
import com.clip.data.abtest.entity.TempAbHomeAdminCardUserGroup;
import com.clip.data.abtest.repository.TempAbHomeAdminCardUserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

//AB_TEST HomeAdminCardUserTest
@Service
@RequiredArgsConstructor
public class TempAbHomeAdminCardUserGroupService {

    private final TempAbHomeAdminCardUserGroupRepository tempAbHomeAdminCardUserGroupRepository;

    /**
     * A/B 테스트 대상 유저 조회
     * @param userId 를 기준으로 Ab Group table에서 사용자의 그룹을 조회합니다.
     * @return 대상 유저의 경유 AbGroup.A or AbGroup.B를 반환하며, 대상 유저가 아닌 경유 AbGroup.NONE 을 반환합니다.
     */
    public AbGroup findUserGroup(Long userId) {
        return tempAbHomeAdminCardUserGroupRepository.findByMemberPk(userId)
                .map(TempAbHomeAdminCardUserGroup::getAbGroup)
                .orElse(AbGroup.NONE);
    }

    public Optional<TempAbHomeAdminCardUserGroup> findTempAbHomeAdminCardUserGroup(Long userId) {
        return tempAbHomeAdminCardUserGroupRepository.findByMemberPk(userId);
    }

    public void incrementClickCount(Long userId) {
        tempAbHomeAdminCardUserGroupRepository.incrementClickCount(userId);
    }

    public void incrementDisplayCount(Long userId) {
        tempAbHomeAdminCardUserGroupRepository.incrementDisplayCount(userId);
    }
}
