package com.clip.data.abtest.repository;

import com.clip.data.abtest.entity.RetrieveType;
import com.clip.data.abtest.entity.TempAbHomeAdminCardUserRetrieveDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//AB_TEST HomeAdminCardUserTest
public interface TempAbHomeAdminCardUserRetrieveDetailRepository extends JpaRepository<TempAbHomeAdminCardUserRetrieveDetail, Long> {

    @Query("select count(t) > 0 from TempAbHomeAdminCardUserRetrieveDetail t " +
            "where t.adminCardUserGroup.member.pk = :userId " +
            "and t.adminFeedCard.pk = :feedCardId " +
            "and t.retrieveType = :retrieveType")
    boolean existsByUserIdAndFeedCardIdAndRetrieveType(
            @Param("userId") Long userId,
            @Param("feedCardId") Long feedCardId,
            @Param("retrieveType") RetrieveType retrieveType
    );
}
