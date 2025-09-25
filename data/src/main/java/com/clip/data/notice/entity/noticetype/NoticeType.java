package com.clip.data.notice.entity.noticetype;

public enum NoticeType {
    ANNOUNCEMENT("공지사항"),
    MAINTENANCE("점검안내");
    private final String description;

    NoticeType(String description) {
        this.description = description;
    }
}
