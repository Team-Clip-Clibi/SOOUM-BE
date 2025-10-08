package com.clip.data.report.entity;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.common.entity.BaseEntity;
import com.clip.data.member.entity.Member;
import com.clip.data.report.entity.reporttype.ReportType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedReport extends BaseEntity {
    @Id @Tsid
    private Long pk;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private ReportType reportType;

    @NotNull
    @JoinColumn(name = "TARGET_CARD", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedCard targetCard;

    @NotNull
    @Column
    private String targetCardContent;

    @NotNull
    @JoinColumn(name = "REPORTER",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Member reporter;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String writerIp;

    @Builder
    public FeedReport(ReportType reportType, FeedCard targetCard, String targetCardContent, Member reporter, String writerIp) {
        this.reportType = reportType;
        this.targetCard = targetCard;
        this.targetCardContent = targetCardContent;
        this.reporter = reporter;
        this.writerIp = writerIp;
    }
}
