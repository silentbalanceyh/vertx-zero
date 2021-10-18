-- liquibase formatted sql

-- changeset Lang:ox-role-perm-1
-- 关联表：R_ROLE_PERM
DROP TABLE IF EXISTS R_ROLE_PERM;
CREATE TABLE IF NOT EXISTS R_ROLE_PERM
(
    `PERM_ID` VARCHAR(36) COMMENT '「permId」- 关联权限ID',
    `ROLE_ID` VARCHAR(36) COMMENT '「roleId」- 关联角色ID',
    PRIMARY KEY
        (
         `PERM_ID`,
         `ROLE_ID`
            ) USING BTREE
);
