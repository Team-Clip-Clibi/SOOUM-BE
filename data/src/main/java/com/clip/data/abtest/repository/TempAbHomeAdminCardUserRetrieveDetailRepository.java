package com.clip.data.abtest.repository;

import com.clip.data.abtest.entity.RetrieveType;
import com.clip.data.abtest.entity.TempAbHomeAdminCardUserRetrieveDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//AB_TEST HomeAdminCardUserTest
public interface TempAbHomeAdminCardUserRetrieveDetailRepository extends JpaRepository<TempAbHomeAdminCardUserRetrieveDetail, Long> {

    @Query(value = "SELECT EXISTS( " +
            "SELECT 1 FROM TempAbHomeAdminCardUserRetrieveDetail t " +
            "JOIN TempAbHomeAdminCardUserGroup g ON t.adminCardUserGroup.id = g.id " +
            "WHERE g.member.pk = :userId AND t.adminFeedCard.pk = :feedCardId AND t.retrieveType = :retrieveType) ")
    boolean existsByUserIdAndFeedCardIdAndRetrieveType(
            @Param("userId") Long userId,
            @Param("feedCardId") Long feedCardId,
            @Param("retrieveType") RetrieveType retrieveType
    );
}
