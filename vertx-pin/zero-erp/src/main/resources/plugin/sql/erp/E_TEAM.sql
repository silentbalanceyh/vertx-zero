-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-team-1
-- ----------------------------
-- Table structure for E_TEAM
-- ----------------------------
DROP TABLE IF EXISTS `E_TEAM`;
CREATE TABLE `E_TEAM`
(
    `KEY`            VARCHAR(36) NOT NULL COMMENT '「key」- 组主键',
    `NAME`           VARCHAR(255) COMMENT '「name」- 组名称',
    `CODE`           VARCHAR(255) COMMENT '「code」- 组编号',

    -- 组长
    `LEADER_ID`      VARCHAR(36) COMMENT '「leaderId」- 组长',
    `LEADER_NAME`    VARCHAR(36) COMMENT '「leaderName」- 组长姓名',
    `DEPT_ID`        VARCHAR(36) COMMENT '「deptId」- 所属部门',
    `TEAM_ID`        VARCHAR(36) COMMENT '「teamId」- 父组ID',
    `COMMENT`        TEXT COMMENT '「comment」- 组备注',

    -- 绑定用户组ID
    `BIND_ID`        VARCHAR(36) COMMENT '「bindId」- 绑定用户组ID,安全专用处理',
    `BIND_COMPONENT` VARCHAR(255) COMMENT '「bindComponent」- 绑定扩展组件',
    `BIND_CONFIG`    LONGTEXT COMMENT '「bindConfig」- 绑定JSON详细配置',

    -- 特殊字段
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:h-team-2
ALTER TABLE E_TEAM
    ADD UNIQUE (`CODE`, `DEPT_ID`) USING BTREE;
ALTER TABLE E_TEAM
    ADD UNIQUE (`NAME`, `DEPT_ID`, `TEAM_ID`) USING BTREE;

ALTER TABLE E_TEAM
    ADD INDEX IDX_E_TEAM_SIGMA (`SIGMA`) USING BTREE;
ALTER TABLE E_TEAM
    ADD INDEX IDX_E_TEAM_SIGMA_ACTIVE (`SIGMA`, `ACTIVE`) USING BTREE;