package com.clip.data.img.entity;


import com.clip.data.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImg extends Img{
    @JoinColumn(name = "PROFILE_OWNER")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member profileOwner;

    @Builder
    public ProfileImg(String imgName, Member profileOwner) {
        super(imgName);
        this.profileOwner = profileOwner;
    }

    public ProfileImg(String imgName) {
        super(imgName);
    }

    public void updateProfileOwner(Member profileOwner) {
        this.profileOwner = profileOwner;
    }
}
