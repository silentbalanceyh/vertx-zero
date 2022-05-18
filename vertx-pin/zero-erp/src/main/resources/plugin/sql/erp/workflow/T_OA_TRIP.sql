-- liquibase formatted sql

-- changeset Lang:t-oa-trip-1
DROP TABLE IF EXISTS T_OA_TRIP;
CREATE TABLE IF NOT EXISTS T_OA_TRIP
(

    `KEY`               VARCHAR(36) COMMENT '「key」- Ticket Primary Key',

    `COMMENT_EXTENSION` LONGTEXT COMMENT '「commentExtension」- Extension Comment',
    -- Request By
    `REQUEST_BY`        VARCHAR(36) COMMENT '「requestBy」- Request User',

    `START_AT`          DATETIME COMMENT '「startAt」- From',
    `END_AT`            DATETIME COMMENT '「endAt」- To',
    `DAYS`              INT COMMENT '「days」- Duration',

    -- Location
    `TRIP_PROVINCE`     VARCHAR(36) COMMENT '「tripProvince」- Trip Province',
    `TRIP_CITY`         VARCHAR(36) COMMENT '「tripCity」- Trip City',
    `TRIP_ADDRESS`      LONGTEXT COMMENT '「tripAddress」- Trip Address',
    `REASON`            LONGTEXT COMMENT '「reason」- The reason to be done',

    -- Assignment Content
    -- WTodo ( AcceptedBy / Comment )
    `WORK_CONTENT`      LONGTEXT COMMENT '「workContent」- Working Assignment Content',
    PRIMARY KEY (`KEY`)
)