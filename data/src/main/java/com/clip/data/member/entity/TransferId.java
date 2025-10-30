package com.clip.data.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferId {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(name = "TRANSFER_ID")
    private String transferId;
}
