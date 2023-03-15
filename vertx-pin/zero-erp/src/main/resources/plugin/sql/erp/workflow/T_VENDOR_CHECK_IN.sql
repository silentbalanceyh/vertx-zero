-- liquibase formatted sql

-- changeset Lang:t-vendor-check-in-1
DROP TABLE IF EXISTS T_VENDOR_CHECK_IN;
CREATE TABLE IF NOT EXISTS T_VENDOR_CHECK_IN
(

    `KEY`               VARCHAR(36) COMMENT '「key」- Ticket Primary Key',

    `COMMENT_EXTENSION` LONGTEXT COMMENT '「commentExtension」- Extension Comment',

    -- SHARED
    `CLASSIFICATION`    VARCHAR(64) COMMENT '「classification」- The ticket related business type',

    `START_AT`          DATETIME COMMENT '「startAt」- From',
    `END_AT`            DATETIME COMMENT '「endAt」- To',
    `DAYS`              INT COMMENT '「days」- Duration',

    -- UNIQUE
    `ONBOARD_AT`        DATETIME COMMENT '「onboardAt」- To',
    PRIMARY KEY (`KEY`)
)