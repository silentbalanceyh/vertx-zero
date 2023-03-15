-- liquibase formatted sql

-- changeset Lang:t-vendor-assessment-1
DROP TABLE IF EXISTS T_VENDOR_ASSESSMENT;
CREATE TABLE IF NOT EXISTS T_VENDOR_ASSESSMENT
(

    `KEY`               VARCHAR(36) COMMENT '「key」- Ticket Primary Key',

    `COMMENT_EXTENSION` LONGTEXT COMMENT '「commentExtension」- Extension Comment',

    -- SHARED
    `CLASSIFICATION`    VARCHAR(64) COMMENT '「classification」- The ticket related business type',

    `START_AT`          DATETIME COMMENT '「startAt」- From',
    `END_AT`            DATETIME COMMENT '「endAt」- To',
    `DAYS`              INT COMMENT '「days」- Duration',

    -- UNIQUE
    `ASSESS_ON`         VARCHAR(36) COMMENT '「assessOn」- The user that will be assessed',
    `ASSESS_SCORE`      INT COMMENT '「assessScore」- The score of the user',

    `COMMENT_DEPT`      LONGTEXT COMMENT '「commentDept」- from department',
    `COMMENT_ASSESS`    LONGTEXT COMMENT '「commentAssess」- from assess',
    `COMMENT_LEADER`    LONGTEXT COMMENT '「commentLeader」- from leader of project',
    PRIMARY KEY (`KEY`)
)