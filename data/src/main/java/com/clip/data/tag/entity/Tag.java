package com.clip.data.tag.entity;


import com.clip.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "COUNT")
    private int count;

    @NotNull
    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @Builder(access = AccessLevel.PRIVATE)
    private Tag(String content, boolean isActive, int count) {
        this.content = content;
        this.isActive = isActive;
        this.count = count;
    }

    public static Tag ofFeed(String content, boolean isActive) {
        return Tag.builder().content(content).isActive(isActive).count(1).build();
    }

    public static Tag ofComment(String content, boolean isActive) {
        return Tag.builder().content(content).isActive(isActive).count(0).build();
    }
}
