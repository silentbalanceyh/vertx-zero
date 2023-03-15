-- liquibase formatted sql

-- changeset Lang:t-oa-vocation-1
DROP TABLE IF EXISTS T_OA_VACATION;
CREATE TABLE IF NOT EXISTS T_OA_VACATION
(

    `KEY`               VARCHAR(36) COMMENT '「key」- Ticket Primary Key',

    `COMMENT_EXTENSION` LONGTEXT COMMENT '「commentExtension」- Extension Comment',
    -- Request By
    `REQUEST_BY`        VARCHAR(36) COMMENT '「requestBy」- Request User',
    `CLASSIFICATION`    VARCHAR(64) COMMENT '「classification」- The ticket related business type',

    `START_AT`          DATETIME COMMENT '「startAt」- From',
    `END_AT`            DATETIME COMMENT '「endAt」- To',
    `DAYS`              INT COMMENT '「days」- Duration',

    -- Location
    `REASON`            LONGTEXT COMMENT '「reason」- The reason to be done',

    -- Assignment Content
    -- WTodo ( AcceptedBy / Comment )
    `WORK_CONTENT`      LONGTEXT COMMENT '「workContent」- Working Assignment Content',
    PRIMARY KEY (`KEY`)
)