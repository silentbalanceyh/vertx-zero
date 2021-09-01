-- liquibase formatted sql

-- changeset Lang:ox-user-group-1
-- 关联表：R_USER_GROUP
DROP TABLE IF EXISTS R_USER_GROUP;
CREATE TABLE IF NOT EXISTS R_USER_GROUP
(
    `GROUP_ID` VARCHAR(36) COMMENT '「groupId」- 关联组ID',
    `USER_ID`  VARCHAR(36) COMMENT '「userId」- 关联用户ID',
    `PRIORITY` INTEGER COMMENT '「priority」- 组优先级',
    PRIMARY KEY
        (
         `GROUP_ID`,
         `USER_ID`
            ) USING BTREE
);

ALTER TABLE R_USER_GROUP
    ADD INDEX IDX_R_USER_GROUP_USER_ID (`USER_ID`) USING BTREE;