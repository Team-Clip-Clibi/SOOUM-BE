package com.clip.data.card.repository;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.repository.projection.DistanceFeedCardDto;
import com.clip.data.member.entity.Member;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FeedCardRepository extends JpaRepository<FeedCard, Long> {
    @Query("select f " +
            "from FeedCard f " +
                "join fetch f.writer " +
            "where (:lastId is null or f.pk < :lastId) " +
                "and f.writer.pk not in :blockMemberPkList " +
                "and (f.isStory=false or (f.isStory = true and f.createdAt > (current_timestamp - 1 day) )) " +
                "and f.isDeleted = false " +
                "and f.isFeedActive = true " +
            "order by f.pk desc")
    List<FeedCard> findByNextPage(@Param("lastId") Long lastId,
                                  @Param("blockMemberPkList") List<Long> blockMemberPkList,
                                  Pageable pageable);


    @Query(
            value = """
        select f.pk as pk,
               f.img_type as imgType,
               f.img_name as imgName,
               f.font as font,
               f.content as content,
               f.created_at as createdAt,
               f.is_story as isStory,
               m.role as role,
               f.location as location
        from feed_card f
        join member m on f.writer = m.pk
        where (:lastPk is null or f.pk < :lastPk)
          and (:blockMemberPks is null or f.writer not in (:blockMemberPks))
          and ST_Contains(ST_Buffer(:userLocation, :distance), f.location)
          and ST_Distance_Sphere(f.location, :userLocation) <= (:distance * 1000)
          and (f.is_story = false or (f.is_story = true and f.created_at > (CURRENT_TIMESTAMP - interval 1 day)))
          and f.is_deleted = false
          and f.is_feed_active = true
        order by f.pk desc
        """,
            nativeQuery = true
    )
    List<DistanceFeedCardDto> findNextByDistance(@Param("lastPk") Long lastPk,
                                                 @Param("userLocation") Point userLocation,
                                                 @Param("distance") double distance,
                                                 @Param("blockMemberPks") List<Long> blockMemberPks,
                                                 Pageable pageable);

    @Query("select count(f) from FeedCard f where f.writer = :cardOwnerMember")
    Long findFeedCardCnt(@Param("cardOwnerMember") Member cardOwnerMember);

    @Query("select fc from FeedCard fc " +
            "where (fc.isStory=false or (fc.isStory = true and fc.createdAt > (current_timestamp - 1 day))) " +
                "and fc.writer.pk = :memberPk " +
                "and (:lastPk is null or fc.pk < :lastPk) " +
            "order by fc.pk desc ")
    List<FeedCard> findMemberFeedCards(@Param("memberPk") Long memberPk,
                                       @Param("lastPk") Long lastPk,
                                       PageRequest pageRequest);

    @Modifying
    @Transactional
    @Query("delete from FeedCard fc WHERE fc.writer.pk = :memberPk")
    void deleteFeedCardByMemberPk(@Param("memberPk") Long memberPk);

    @Query("select f from FeedCard f join fetch f.writer where f.pk = :feedCardPk")
    Optional<FeedCard> findWithMember(@Param("feedCardPk") Long feedCardPk);
}
