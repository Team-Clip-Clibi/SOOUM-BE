package com.clip.data.img.entity;

import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Img extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @Column(name = "IMG_NAME")
    private String imgName;

    public Img(String imgName) {
        this.imgName = imgName;
    }
}