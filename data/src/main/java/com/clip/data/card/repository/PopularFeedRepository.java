package com.clip.data.card.repository;

import com.clip.data.card.entity.PopularFeed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PopularFeedRepository extends JpaRepository<PopularFeed, Long> {

    @Query("select pc from PopularFeed pc " +
                "inner join pc.popularCard f " +
                "join fetch pc.popularCard " +
            "where f.writer.pk not in :blockedMembers " +
            "and ((pc.version = :likeVersion and pc.popularityType = 'LIKE') " +
                "or (pc.version = :commentVersion and pc.popularityType = 'COMMENT'))" +
            "order by pc.pk asc")
    List<PopularFeed> findPopularFeeds(@Param("blockedMembers") List<Long> blockedMembers,
                                    @Param("likeVersion") int likeVersion,
                                    @Param("commentVersion") int commentVersion,
                                    Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from PopularFeed pf where pf.popularCard.pk = :popularCardPk")
    void deletePopularCard(@Param("popularCardPk") Long popularCardPk);

    @Modifying
    @Transactional
    @Query("delete from PopularFeed pf where pf.popularCard.writer.pk = :memberPk")
    void deletePopularCardByMemberPk(@Param("memberPk") Long memberPk);

    @Query("select max(pf.version) from PopularFeed pf where pf.popularityType = 'LIKE'")
    Optional<Integer> findLatestVersionByLike();

    @Query("select max(pf.version) from PopularFeed pf where pf.popularityType = 'COMMENT'")
    Optional<Integer> findLatestVersionByComment();
}
