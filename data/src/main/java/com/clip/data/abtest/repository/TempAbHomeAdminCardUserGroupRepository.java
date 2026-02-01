package com.clip.data.abtest.repository;

import com.clip.data.abtest.entity.TempAbHomeAdminCardUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//AB_TEST HomeAdminCardUserTest
public interface TempAbHomeAdminCardUserGroupRepository extends JpaRepository<TempAbHomeAdminCardUserGroup, Long> {

    @Query("select t from TempAbHomeAdminCardUserGroup t where t.member.pk = :userId")
    Optional<TempAbHomeAdminCardUserGroup> findByMemberPk(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update TempAbHomeAdminCardUserGroup t set t.clickCount = t.clickCount + 1 where t.member.pk = :userId")
    void incrementClickCount(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update TempAbHomeAdminCardUserGroup t set t.displayCount = t.displayCount + 1 where t.member.pk = :userId")
    void incrementDisplayCount(@Param("userId") Long userId);
}
