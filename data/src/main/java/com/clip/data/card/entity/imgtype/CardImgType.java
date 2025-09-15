package com.clip.data.card.entity.imgtype;

import lombok.Getter;

@Getter
public enum CardImgType {
    DEFAULT("default"),
    USER("user");

    private final String imgPath;

    CardImgType(String imgPath) {
        this.imgPath = imgPath;
    }
}
