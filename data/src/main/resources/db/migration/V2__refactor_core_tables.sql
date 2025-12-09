ALTER TABLE comment_report
    DROP FOREIGN KEY FK810pbys9qqdy190b6h0iacvy8;

ALTER TABLE comment_report
    DROP FOREIGN KEY FKpm2nj0qeuh08sa847jm9ukaie;

ALTER TABLE feed_report
    DROP FOREIGN KEY FKr1k4wx0kug19frirdf1t5fbqg;

CREATE TABLE account_transfer_history
(
    pk          BIGINT   NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    member      BIGINT   NOT NULL,
    transfer_at datetime NOT NULL,
    CONSTRAINT pk_accounttransferhistory PRIMARY KEY (pk)
);

CREATE TABLE comment_view
(
    pk             BIGINT   NOT NULL,
    created_at     datetime NULL,
    updated_at     datetime NULL,
    visit_date     date     NOT NULL,
    target_comment BIGINT   NULL,
    visitor        BIGINT   NULL,
    CONSTRAINT pk_commentview PRIMARY KEY (pk)
);

CREATE TABLE feed_view
(
    pk          BIGINT   NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    visit_date  date     NOT NULL,
    target_feed BIGINT   NULL,
    visitor     BIGINT   NULL,
    CONSTRAINT pk_feedview PRIMARY KEY (pk)
);

CREATE TABLE member_withdrawal_reason
(
    pk         BIGINT       NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    reason     VARCHAR(255) NULL,
    CONSTRAINT pk_memberwithdrawalreason PRIMARY KEY (pk)
);

ALTER TABLE popular_feed
    ADD create_version VARCHAR(255) NULL;

ALTER TABLE popular_feed
    ADD updated_at datetime NULL;

ALTER TABLE popular_feed
    MODIFY create_version VARCHAR(255) NOT NULL;

ALTER TABLE member
    ADD device_model VARCHAR(255) NULL;

ALTER TABLE member
    ADD device_os_version VARCHAR(255) NULL;

ALTER TABLE member
    ADD updated_at datetime NULL;

ALTER TABLE member
    MODIFY device_model VARCHAR(255) NOT NULL;

ALTER TABLE member
    MODIFY device_os_version VARCHAR(255) NOT NULL;

ALTER TABLE notification_history
    ADD tag_content TEXT NULL;

ALTER TABLE notification_history
    ADD updated_at datetime NULL;

ALTER TABLE account_transfer
    ADD updated_at datetime NULL;

ALTER TABLE app_version
    ADD updated_at datetime NULL;

ALTER TABLE blacklist
    ADD updated_at datetime NULL;

ALTER TABLE block
    ADD updated_at datetime NULL;

ALTER TABLE card_img
    ADD updated_at datetime NULL;

ALTER TABLE comment_card
    ADD updated_at datetime NULL;

ALTER TABLE comment_like
    ADD updated_at datetime NULL;

ALTER TABLE comment_report
    ADD updated_at datetime NULL;

ALTER TABLE comment_tag
    ADD updated_at datetime NULL;

ALTER TABLE favorite_tag
    ADD updated_at datetime NULL;

ALTER TABLE feed_card
    ADD updated_at datetime NULL;

ALTER TABLE feed_like
    ADD updated_at datetime NULL;

ALTER TABLE feed_report
    ADD updated_at datetime NULL;

ALTER TABLE feed_tag
    ADD updated_at datetime NULL;

ALTER TABLE follow
    ADD updated_at datetime NULL;

ALTER TABLE policy_term
    ADD updated_at datetime NULL;

ALTER TABLE profile_img
    ADD updated_at datetime NULL;

ALTER TABLE suspended
    ADD updated_at datetime NULL;

ALTER TABLE tag
    ADD updated_at datetime NULL;

ALTER TABLE visitor
    ADD updated_at datetime NULL;

ALTER TABLE favorite_tag
    ADD CONSTRAINT uc_favoritetag UNIQUE (member, tag);

ALTER TABLE block
    ADD CONSTRAINT uk_block_from_to UNIQUE (from_member, to_member);

ALTER TABLE comment_like
    ADD CONSTRAINT uk_comment_like_card_member UNIQUE (target_card, liked_member);

ALTER TABLE feed_like
    ADD CONSTRAINT uk_feed_like_card_member UNIQUE (target_card, liked_member);

CREATE INDEX IDX_VISIT_DATE ON feed_view (visit_date);

CREATE INDEX IDX_VISIT_DATE ON feed_view (visit_date);

ALTER TABLE account_transfer_history
    ADD CONSTRAINT FK_ACCOUNTTRANSFERHISTORY_ON_MEMBER FOREIGN KEY (member) REFERENCES member (pk);


DROP TABLE app_version_flag;

ALTER TABLE notification_history
    DROP COLUMN content;

ALTER TABLE notification_history
    DROP COLUMN font;

ALTER TABLE notification_history
    DROP COLUMN font_size;

ALTER TABLE notification_history
    DROP COLUMN img_name;

ALTER TABLE notification_history
    DROP COLUMN img_type;

ALTER TABLE notification_history
    DROP COLUMN notification_type;

ALTER TABLE comment_card
    DROP COLUMN font_size;

ALTER TABLE comment_card
    DROP COLUMN font;

ALTER TABLE comment_card
    DROP COLUMN img_type;

ALTER TABLE comment_card
    DROP COLUMN parent_card_type;

ALTER TABLE feed_card
    DROP COLUMN font_size;

ALTER TABLE feed_card
    DROP COLUMN is_public;

ALTER TABLE feed_card
    DROP COLUMN font;

ALTER TABLE feed_card
    DROP COLUMN img_type;

ALTER TABLE popular_feed
    DROP COLUMN popularity_type;

ALTER TABLE popular_feed
    DROP COLUMN version;

ALTER TABLE tag
    DROP COLUMN version;

ALTER TABLE app_version
    DROP COLUMN device_type;

ALTER TABLE app_version
    ADD device_type VARCHAR(255) NOT NULL;

ALTER TABLE member
    DROP COLUMN device_type;

ALTER TABLE member
    DROP COLUMN `role`;

ALTER TABLE member
    ADD device_type VARCHAR(255) NOT NULL;

ALTER TABLE comment_card
    ADD font VARCHAR(255) NOT NULL;

ALTER TABLE feed_card
    ADD font VARCHAR(255) NOT NULL;

ALTER TABLE comment_card
    ADD img_type VARCHAR(255) NOT NULL;

ALTER TABLE feed_card
    ADD img_type VARCHAR(255) NOT NULL;

ALTER TABLE notice
    DROP COLUMN notice_type;

ALTER TABLE notice
    ADD notice_type VARCHAR(255) NULL;

ALTER TABLE notification_history
    ADD notification_type VARCHAR(255) NOT NULL;

ALTER TABLE comment_card
    ADD parent_card_type VARCHAR(255) NULL;

ALTER TABLE comment_report
    DROP COLUMN report_type;

ALTER TABLE comment_report
    ADD report_type VARCHAR(255) NOT NULL;

ALTER TABLE feed_report
    DROP COLUMN report_type;

ALTER TABLE feed_report
    ADD report_type VARCHAR(255) NOT NULL;

ALTER TABLE member
    ADD `role` VARCHAR(255) NULL;