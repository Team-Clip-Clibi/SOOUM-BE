package com.clip.data.abtest.repository;

import com.clip.data.abtest.entity.RetrieveType;
import com.clip.data.abtest.entity.TempAbHomeAdminCardUserRetrieveDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//AB_TEST HomeAdminCardUserTest
public interface TempAbHomeAdminCardUserRetrieveDetailRepository extends JpaRepository<TempAbHomeAdminCardUserRetrieveDetail, Long> {

    @Query(value = "SELECT EXISTS( " +
            "SELECT 1 FROM temp_ab_home_admin_card_user_retrieve_detail t " +
            "JOIN temp_ab_home_admin_card_user_group g ON t.admin_card_user_group_id = g.pk " +
            "WHERE g.member_id = :userId AND t.admin_feed_card_id = :feedCardId AND t.retrieve_type = :retrieveType) ",
            nativeQuery = true)
    boolean existsByUserIdAndFeedCardIdAndRetrieveType(
            @Param("userId") Long userId,
            @Param("feedCardId") Long feedCardId,
            @Param("retrieveType") RetrieveType retrieveType
    );
}
