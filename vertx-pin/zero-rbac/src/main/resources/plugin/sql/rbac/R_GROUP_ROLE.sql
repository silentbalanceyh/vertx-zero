-- liquibase formatted sql

-- changeset Lang:ox-group-role-1
-- 关联表：R_GROUP_ROLE
DROP TABLE IF EXISTS R_GROUP_ROLE;
CREATE TABLE IF NOT EXISTS R_GROUP_ROLE
(
    `GROUP_ID` VARCHAR(36) COMMENT '「groupId」- 关联组ID',
    `ROLE_ID`  VARCHAR(36) COMMENT '「roleId」- 关联角色ID',
    `PRIORITY` INTEGER COMMENT '「priority」- 角色优先级',
    PRIMARY KEY (`GROUP_ID`, `ROLE_ID`)
);
