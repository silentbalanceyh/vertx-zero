-- liquibase formatted sql

-- changeset Lang:ox-user-role-1
-- 关联表：R_USER_ROLE
DROP TABLE IF EXISTS R_USER_ROLE;
CREATE TABLE IF NOT EXISTS R_USER_ROLE
(
    `USER_ID`  VARCHAR(36) COMMENT '「userId」- 关联用户ID',
    `ROLE_ID`  VARCHAR(36) COMMENT '「roleId」- 关联角色ID',
    `PRIORITY` INTEGER COMMENT '「priority」- 角色优先级',
    PRIMARY KEY
        (
         `USER_ID`,
         `ROLE_ID`
            ) USING BTREE
);

ALTER TABLE R_USER_ROLE
    ADD INDEX IDX_R_USER_ROLE_USER_ID (`USER_ID`) USING BTREE;