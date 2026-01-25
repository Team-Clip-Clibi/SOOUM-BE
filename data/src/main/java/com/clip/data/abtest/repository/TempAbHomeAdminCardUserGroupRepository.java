package com.clip.data.abtest.repository;

import com.clip.data.abtest.entity.TempAbHomeAdminCardUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//AB_TEST HomeAdminCardUserTest
public interface TempAbHomeAdminCardUserGroupRepository extends JpaRepository<TempAbHomeAdminCardUserGroup, Long> {

    @Query("select t from TempAbHomeAdminCardUserGroup t where t.member.pk = :userId")
    Optional<TempAbHomeAdminCardUserGroup> findByMemberPk(@Param("userId") Long userId);
}
