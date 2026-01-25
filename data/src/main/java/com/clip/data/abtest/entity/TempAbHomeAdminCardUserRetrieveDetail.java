package com.clip.data.abtest.entity;

import com.clip.data.card.entity.FeedCard;
import com.clip.data.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "temp_ab_home_admin_card_user_retrieve_detail")
@NoArgsConstructor
public class TempAbHomeAdminCardUserRetrieveDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_card_user_group_id", nullable = false)
    private TempAbHomeAdminCardUserGroup adminCardUserGroup;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_feed_card_id", nullable = false)
    private FeedCard adminFeedCard;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "retrieve_type", nullable = false)
    private RetrieveType retrieveType;

    @Builder
    public TempAbHomeAdminCardUserRetrieveDetail(TempAbHomeAdminCardUserGroup adminCardUserGroup, FeedCard adminFeedCard, RetrieveType retrieveType) {
        this.adminCardUserGroup = adminCardUserGroup;
        this.adminFeedCard = adminFeedCard;
        this.retrieveType = retrieveType;
    }
}