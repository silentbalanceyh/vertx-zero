-- liquibase formatted sql

-- changeset Lang:t-vendor-hour-1
DROP TABLE IF EXISTS T_VENDOR_HOUR;
CREATE TABLE IF NOT EXISTS T_VENDOR_HOUR
(

    `KEY`               VARCHAR(36) COMMENT '「key」- Ticket Primary Key',

    `COMMENT_EXTENSION` LONGTEXT COMMENT '「commentExtension」- Extension Comment',

    -- SHARED
    `CLASSIFICATION`    VARCHAR(64) COMMENT '「classification」- The ticket related business type',

    `START_AT`          DATETIME COMMENT '「startAt」- From',
    `END_AT`            DATETIME COMMENT '「endAt」- To',
    `DAYS`              INT COMMENT '「days」- Duration',

    -- UNIQUE
    `REQUEST_TYPE`      VARCHAR(64) COMMENT '「requestType」- Request type of hour',
    `FROM_TYPE`         VARCHAR(36) COMMENT '「fromType」',
    `FROM_AT`           DATETIME COMMENT '「fromAt」',
    `TO_TYPE`           VARCHAR(36) COMMENT '「toType」',
    `TO_AT`             DATETIME COMMENT '「toAt」',

    PRIMARY KEY (`KEY`)
)