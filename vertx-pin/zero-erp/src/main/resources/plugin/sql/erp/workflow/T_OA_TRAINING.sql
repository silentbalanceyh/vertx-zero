-- liquibase formatted sql

-- changeset Lang:t-oa-training-1
DROP TABLE IF EXISTS T_OA_TRAINING;
CREATE TABLE IF NOT EXISTS T_OA_TRAINING
(

    `KEY`               VARCHAR(36) COMMENT '「key」- Ticket Primary Key',

    `COMMENT_EXTENSION` LONGTEXT COMMENT '「commentExtension」- Extension Comment',

    `START_AT`          DATETIME COMMENT '「startAt」- From',
    `END_AT`            DATETIME COMMENT '「endAt」- To',

    `TRAIN_LOCATION`    LONGTEXT COMMENT '「trainLocation」- The location for training',
    `TRAIN_MODE`        VARCHAR(64) COMMENT '「trainMode」- The mode of training',

    `LEADER`            VARCHAR(36) COMMENT '「leader」- The training leader',
    `LEADER_COMMENT`    LONGTEXT COMMENT '「leaderComment」- Comment from leader',

    `REVIEWER`          VARCHAR(36) COMMENT '「reviewer」- The training reviewer',
    `REVIEWER_COMMENT`  LONGTEXT COMMENT '「reviewerComment」- Comment from reviewer',

    PRIMARY KEY (`KEY`)
)