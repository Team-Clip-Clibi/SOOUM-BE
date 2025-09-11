package com.clip.data.img.repository;

import com.clip.data.img.entity.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {
    @Modifying
    @Transactional
    @Query("update ProfileImg pi set pi.profileOwner = null where pi.profileOwner.pk = :memberPk")
    void updateCardImgNull(@Param("memberPk") Long memberPk);

    @Query("select pi from ProfileImg pi where pi.imgName = :profileImgName")
    Optional<ProfileImg> findProfileImg(@Param("profileImgName") String profileImgName);
}
