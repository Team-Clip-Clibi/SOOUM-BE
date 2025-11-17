package com.clip.data.notice.repository;

import com.clip.data.notice.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select n from Notice n where (:lastPk is null or n.pk < :lastPk) and n.date >= :since order by n.pk desc")
    List<Notice> findNoticesForNotification(@Param("lastPk") Long lastPk,
                                            @Param("since") LocalDate since,
                                            Pageable pageable);

    @Query("select n from Notice n where (:lastPk is null or n.pk < :lastPk) order by n.pk desc ")
    List<Notice> findNoticeForSettings(@Param("lastPk") Long lastPk, Pageable pageable);
}
