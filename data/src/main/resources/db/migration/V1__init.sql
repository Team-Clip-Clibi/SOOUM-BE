CREATE TABLE account_transfer
(
    created_at      datetime     NULL,
    expiration_date datetime     NOT NULL,
    member          BIGINT       NOT NULL,
    pk              BIGINT       NOT NULL,
    transfer_id     VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE app_version
(
    created_at      datetime     NULL,
    pk              BIGINT       NOT NULL,
    latest_version  VARCHAR(255) NULL,
    device_type     ENUM         NOT NULL,
    min_version     VARCHAR(255) NULL,
    pending_version VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE app_version_flag
(
    pk      BIGINT NOT NULL,
    is_flag BIT(1) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE blacklist
(
    created_at datetime     NULL,
    expired_at datetime     NULL,
    token      VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (token)
);

CREATE TABLE block
(
    created_at  datetime NULL,
    from_member BIGINT   NOT NULL,
    pk          BIGINT   NOT NULL,
    to_member   BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE card_img
(
    comment_card BIGINT       NULL,
    created_at   datetime     NULL,
    feed_card    BIGINT       NULL,
    pk           BIGINT       NOT NULL,
    img_name     VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE comment_card
(
    is_deleted       BIT(1)       NULL,
    created_at       datetime     NULL,
    master_card_pk   BIGINT       NOT NULL,
    parent_card_pk   BIGINT       NULL,
    pk               BIGINT       NOT NULL,
    writer           BIGINT       NULL,
    content          LONGTEXT     NOT NULL,
    img_name         VARCHAR(255) NULL,
    font             ENUM         NOT NULL,
    font_size        ENUM         NOT NULL,
    img_type         ENUM         NOT NULL,
    location         GEOMETRY     NULL,
    parent_card_type ENUM         NULL,
    writer_ip        VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE comment_like
(
    is_deleted   BIT(1)   NULL,
    created_at   datetime NULL,
    liked_member BIGINT   NOT NULL,
    pk           BIGINT   NOT NULL,
    target_card  BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE comment_report
(
    created_at          datetime     NULL,
    pk                  BIGINT       NOT NULL,
    reporter            BIGINT       NOT NULL,
    target_card         BIGINT       NOT NULL,
    report_type         ENUM         NOT NULL,
    writer_ip           VARCHAR(255) NOT NULL,
    target_card_content LONGTEXT     NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE comment_tag
(
    comment_card BIGINT   NOT NULL,
    created_at   datetime NULL,
    pk           BIGINT   NOT NULL,
    tag          BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE favorite_tag
(
    created_at datetime NULL,
    member     BIGINT   NOT NULL,
    pk         BIGINT   NOT NULL,
    tag        BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE fcm_scheduler_content
(
    pk      BIGINT       NOT NULL,
    content VARCHAR(255) NULL,
    title   VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE feed_card
(
    is_deleted     BIT(1)       NULL,
    is_public      BIT(1)       NULL,
    is_story       BIT(1)       NULL,
    created_at     datetime     NULL,
    pk             BIGINT       NOT NULL,
    writer         BIGINT       NULL,
    content        LONGTEXT     NOT NULL,
    img_name       VARCHAR(255) NULL,
    font           ENUM         NOT NULL,
    font_size      ENUM         NOT NULL,
    img_type       ENUM         NOT NULL,
    location       GEOMETRY     NULL,
    is_feed_active BIT(1)       NULL,
    writer_ip      VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE feed_like
(
    is_deleted   BIT(1)   NULL,
    created_at   datetime NULL,
    liked_member BIGINT   NOT NULL,
    pk           BIGINT   NOT NULL,
    target_card  BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE feed_report
(
    created_at          datetime     NULL,
    pk                  BIGINT       NOT NULL,
    reporter            BIGINT       NOT NULL,
    target_card         BIGINT       NOT NULL,
    report_type         ENUM         NOT NULL,
    writer_ip           VARCHAR(255) NOT NULL,
    target_card_content VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE feed_tag
(
    created_at datetime NULL,
    feed_card  BIGINT   NOT NULL,
    pk         BIGINT   NOT NULL,
    tag        BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE follow
(
    created_at  datetime NULL,
    from_member BIGINT   NOT NULL,
    pk          BIGINT   NOT NULL,
    to_member   BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE member
(
    ban_count         INT          NULL,
    is_allow_notify   BIT(1)       NULL,
    created_at        datetime     NULL,
    deleted_at        datetime     NULL,
    pk                BIGINT       NOT NULL,
    total_visitor_cnt BIGINT       NOT NULL,
    until_ban         datetime     NULL,
    device_id         VARCHAR(255) NOT NULL,
    firebase_token    BLOB         NULL,
    nickname          BLOB         NOT NULL,
    profile_img_name  VARCHAR(255) NULL,
    device_type       ENUM         NOT NULL,
    `role`            ENUM         NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE notice
(
    date        date         NULL,
    pk          BIGINT       NOT NULL,
    title       VARCHAR(255) NULL,
    url         VARCHAR(255) NULL,
    notice_type ENUM         NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE notification_history
(
    is_read           BIT(1)       NULL,
    created_at        datetime     NULL,
    from_member       BIGINT       NULL,
    pk                BIGINT       NOT NULL,
    read_at           datetime     NULL,
    target_card_pk    BIGINT       NULL,
    to_member         BIGINT       NOT NULL,
    content           LONGTEXT     NULL,
    img_name          VARCHAR(255) NULL,
    font              ENUM         NULL,
    font_size         ENUM         NULL,
    img_type          ENUM         NULL,
    notification_type ENUM         NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE policy_term
(
    is_allow_term_one   BIT(1)   NULL,
    is_allow_term_three BIT(1)   NULL,
    is_allow_term_two   BIT(1)   NULL,
    created_at          datetime NULL,
    member              BIGINT   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (member)
);

CREATE TABLE popular_feed
(
    version         INT      NULL,
    created_at      datetime NULL,
    pk              BIGINT   NOT NULL,
    popular_card    BIGINT   NOT NULL,
    popularity_type ENUM     NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE profile_img
(
    created_at    datetime     NULL,
    pk            BIGINT       NOT NULL,
    profile_owner BIGINT       NULL,
    img_name      VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE refresh_token
(
    member        BIGINT       NOT NULL,
    refresh_token VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (member)
);

CREATE TABLE rsa
(
    expired_at  datetime              NULL,
    id          BIGINT AUTO_INCREMENT NOT NULL,
    private_key LONGTEXT              NULL,
    public_key  LONGTEXT              NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE suspended
(
    created_at  datetime     NULL,
    pk          BIGINT       NOT NULL,
    until_ban   datetime     NOT NULL,
    device_id   VARCHAR(255) NOT NULL,
    is_ban_user BIT(1)       NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE tag
(
    count      INT          NULL,
    is_active  BIT(1)       NOT NULL,
    created_at datetime     NULL,
    pk         BIGINT       NOT NULL,
    content    VARCHAR(255) NOT NULL,
    version    BIGINT       NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE transfer_id
(
    pk          BIGINT AUTO_INCREMENT NOT NULL,
    transfer_id VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

CREATE TABLE visitor
(
    visit_date    date     NOT NULL,
    created_at    datetime NULL,
    pk            BIGINT   NOT NULL,
    profile_owner BIGINT   NULL,
    visitor       BIGINT   NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (pk)
);

ALTER TABLE member
    ADD CONSTRAINT UK9jyf8xmtcaqhop1bptkblu47g UNIQUE (device_id);

ALTER TABLE suspended
    ADD CONSTRAINT UKkj9m9c4hniq5ap14cf2a1yw1c UNIQUE (device_id);

ALTER TABLE account_transfer
    ADD CONSTRAINT UKsc6da3xn86rqr6pcfjnargekk UNIQUE (transfer_id);

CREATE INDEX FK46049c7tqmr8x7ux7whcwb3p2 ON feed_report (target_card);

CREATE INDEX IDX_VISIT_DATE ON visitor (visit_date);

ALTER TABLE comment_like
    ADD CONSTRAINT FK3ffutjl8jqt14jtnxy22g4tur FOREIGN KEY (liked_member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FK3ffutjl8jqt14jtnxy22g4tur ON comment_like (liked_member);

ALTER TABLE profile_img
    ADD CONSTRAINT FK57rlp7qdmktxcopront8p1u6r FOREIGN KEY (profile_owner) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FK57rlp7qdmktxcopront8p1u6r ON profile_img (profile_owner);

ALTER TABLE card_img
    ADD CONSTRAINT FK6vxmk4poyn1ommrw6q9rtspa6 FOREIGN KEY (feed_card) REFERENCES feed_card (pk) ON DELETE NO ACTION;

CREATE INDEX FK6vxmk4poyn1ommrw6q9rtspa6 ON card_img (feed_card);

ALTER TABLE feed_tag
    ADD CONSTRAINT FK7yi1jmml1b3mqpba5ermaib1i FOREIGN KEY (tag) REFERENCES tag (pk) ON DELETE NO ACTION;

CREATE INDEX FK7yi1jmml1b3mqpba5ermaib1i ON feed_tag (tag);

ALTER TABLE comment_report
    ADD CONSTRAINT FK810pbys9qqdy190b6h0iacvy8 FOREIGN KEY (reporter) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FK810pbys9qqdy190b6h0iacvy8 ON comment_report (reporter);

ALTER TABLE block
    ADD CONSTRAINT FK96ub09oq6gc9p27p23e6wvu8 FOREIGN KEY (from_member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FK96ub09oq6gc9p27p23e6wvu8 ON block (from_member);

ALTER TABLE favorite_tag
    ADD CONSTRAINT FKb7xs8900hd1l773xk1qprn312 FOREIGN KEY (member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FKb7xs8900hd1l773xk1qprn312 ON favorite_tag (member);

ALTER TABLE favorite_tag
    ADD CONSTRAINT FKc81hjihg7o66a4y5j0q9e1lrw FOREIGN KEY (tag) REFERENCES tag (pk) ON DELETE NO ACTION;

CREATE INDEX FKc81hjihg7o66a4y5j0q9e1lrw ON favorite_tag (tag);

ALTER TABLE follow
    ADD CONSTRAINT FKf217m522s4rxjlskf7osv1j42 FOREIGN KEY (to_member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FKf217m522s4rxjlskf7osv1j42 ON follow (to_member);

ALTER TABLE block
    ADD CONSTRAINT FKf49tune1itkq6weq95h1nwtoa FOREIGN KEY (to_member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FKf49tune1itkq6weq95h1nwtoa ON block (to_member);

ALTER TABLE comment_like
    ADD CONSTRAINT FKhabc45nq4hrudj7by8whghp0d FOREIGN KEY (target_card) REFERENCES comment_card (pk) ON DELETE NO ACTION;

CREATE INDEX FKhabc45nq4hrudj7by8whghp0d ON comment_like (target_card);

ALTER TABLE policy_term
    ADD CONSTRAINT FKhrm775u6ogy8ilblgmcb0vemt FOREIGN KEY (member) REFERENCES member (pk) ON DELETE NO ACTION;

ALTER TABLE follow
    ADD CONSTRAINT FKiocu8anmamh4wr0xbg6t39kts FOREIGN KEY (from_member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FKiocu8anmamh4wr0xbg6t39kts ON follow (from_member);

ALTER TABLE card_img
    ADD CONSTRAINT FKmlqkm45k9d1mxtad96dxooaf2 FOREIGN KEY (comment_card) REFERENCES comment_card (pk) ON DELETE NO ACTION;

CREATE INDEX FKmlqkm45k9d1mxtad96dxooaf2 ON card_img (comment_card);

ALTER TABLE refresh_token
    ADD CONSTRAINT FKn3m5lap6p90vcnwhkk9tfeilk FOREIGN KEY (member) REFERENCES member (pk) ON DELETE NO ACTION;

ALTER TABLE feed_tag
    ADD CONSTRAINT FKp453v38lmenpqg01i01db7097 FOREIGN KEY (feed_card) REFERENCES feed_card (pk) ON DELETE NO ACTION;

CREATE INDEX FKp453v38lmenpqg01i01db7097 ON feed_tag (feed_card);

ALTER TABLE comment_report
    ADD CONSTRAINT FKpm2nj0qeuh08sa847jm9ukaie FOREIGN KEY (target_card) REFERENCES comment_card (pk) ON DELETE NO ACTION;

CREATE INDEX FKpm2nj0qeuh08sa847jm9ukaie ON comment_report (target_card);

ALTER TABLE comment_tag
    ADD CONSTRAINT FKqsx7ndqaw9ylhukjuqvc6to6j FOREIGN KEY (tag) REFERENCES tag (pk) ON DELETE NO ACTION;

CREATE INDEX FKqsx7ndqaw9ylhukjuqvc6to6j ON comment_tag (tag);

ALTER TABLE account_transfer
    ADD CONSTRAINT FKqubliw07eg1fkr94rf144q7je FOREIGN KEY (member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FKqubliw07eg1fkr94rf144q7je ON account_transfer (member);

ALTER TABLE feed_report
    ADD CONSTRAINT FKr1k4wx0kug19frirdf1t5fbqg FOREIGN KEY (reporter) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FKr1k4wx0kug19frirdf1t5fbqg ON feed_report (reporter);

ALTER TABLE popular_feed
    ADD CONSTRAINT FKs51yhjq33x3be7wpmatvvnybs FOREIGN KEY (popular_card) REFERENCES feed_card (pk) ON DELETE NO ACTION;

CREATE INDEX FKs51yhjq33x3be7wpmatvvnybs ON popular_feed (popular_card);

ALTER TABLE feed_like
    ADD CONSTRAINT FKsvc5vevuhnxrfo78bmb8haj96 FOREIGN KEY (target_card) REFERENCES feed_card (pk) ON DELETE NO ACTION;

CREATE INDEX FKsvc5vevuhnxrfo78bmb8haj96 ON feed_like (target_card);

ALTER TABLE feed_like
    ADD CONSTRAINT FKtev3nffyft0ibbal2mc25xe4l FOREIGN KEY (liked_member) REFERENCES member (pk) ON DELETE NO ACTION;

CREATE INDEX FKtev3nffyft0ibbal2mc25xe4l ON feed_like (liked_member);

ALTER TABLE comment_tag
    ADD CONSTRAINT FKtr4pl780hwb3k3iqh47lw9dp0 FOREIGN KEY (comment_card) REFERENCES comment_card (pk) ON DELETE NO ACTION;

CREATE INDEX FKtr4pl780hwb3k3iqh47lw9dp0 ON comment_tag (comment_card);
