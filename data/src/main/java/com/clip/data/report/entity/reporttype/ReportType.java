package com.clip.data.report.entity.reporttype;

public enum ReportType {
    DEFAMATION_AND_ABUSE("비방 및 욕설"),
    PRIVACY_VIOLATION("개인정보 침해"),
    INAPPROPRIATE_ADVERTISING("부적절한 홍보 및 바이럴"),
    PORNOGRAPHY("음란물"),
    IMPERSONATION_AND_FRAUD("사칭 및 사기"),
    OTHER("기타");

    private final String content;

    public String getContent() {
        return content;
    }

    ReportType(String content) {
        this.content = content;
    }
}
