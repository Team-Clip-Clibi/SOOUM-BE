package com.clip.data.member.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    USER("ROLE_USER"),
    BANNED("ROLE_BANNED");

    private final String field;

    Role(String field) {
        this.field = field;
    }

    public static Role getRole(String s) {
        return Arrays.stream(values())
                .filter(role -> role.toString().equals(s))
                .findAny()
                .orElse(USER);
    }
}
