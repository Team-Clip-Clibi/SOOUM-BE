package com.clip.data.abtest.entity;

import com.clip.data.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

//AB_TEST HomeAdminCardUserTest
@Getter
@Entity
@Table(name = "temp_ab_home_admin_card_user_group")
@NoArgsConstructor
public class TempAbHomeAdminCardUserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "ab_group")
    private AbGroup abGroup;

    @NotNull
    @Column(name = "display_count", nullable = false)
    private Integer displayCount;
    @NotNull
    @Column(name = "click_count", nullable = false)
    private Integer clickCount;
}