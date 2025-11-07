package com.clip.data.tag.repository;

import com.clip.data.tag.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t " +
            "WHERE (t.isActive = true " +
            "   AND (t.content LIKE CONCAT(:keyword, '%')) " +
            "   or (t.content = :keyword )) " +
            "ORDER BY t.count DESC")
    List<Tag> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select t from Tag t where t.content in :tagContents")
    List<Tag> findTagList(@Param("tagContents") List<String> tagContents);

    @Query("select t " +
            "from Tag t " +
            "where t not in :excludeTags " +
            "   and (t.count > 0) " +
            "   and t.isActive = true " +
            "   and t.content not in :deactivateWords " +
            "order by t.count desc")
    List<Tag> findRecommendTagList(@Param("excludeTags") List<Tag> excludeTags,
                                   @Param("deactivateWords") List<String> deactivateWords,
                                   Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Tag t set t.count = t.count + 1 where t in :tags")
    void incrementTagCount(@Param("tags") List<Tag> tags);

    @Modifying
    @Transactional
    @Query("update Tag t set t.count = t.count - 1 where t in :tags")
    void decrementTagCount(@Param("tags") List<Tag> tags);

    @Query("select t from Tag t where t.content not in :tagContents order by t.count desc limit 10")
    List<Tag> findTop10Tags(@Param("tagContents") List<String> tagContents);
}
