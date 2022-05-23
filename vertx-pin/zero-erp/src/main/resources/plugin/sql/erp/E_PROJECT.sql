-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-project-1
-- ----------------------------
-- Table structure for E_PROJECT
-- ----------------------------
DROP TABLE IF EXISTS `E_PROJECT`;
CREATE TABLE `E_PROJECT`
(
    `KEY`         VARCHAR(36) NOT NULL COMMENT '「key」- 项目ID',
    `NAME`        VARCHAR(255) COMMENT '「name」- 项目名称',
    `CODE`        VARCHAR(255) COMMENT '「code」- 项目编号',
    `TYPE`        VARCHAR(36) COMMENT '「type」- 项目分类（不同类型代表不同项目）',
    `STATUS`      VARCHAR(36) COMMENT '「status」- 项目状态',
    -- 项目基本信息
    `TITLE`       VARCHAR(1024) COMMENT '「title」- 主单业务标题',
    `DESCRIPTION` LONGTEXT COMMENT '「description」- 主单描述内容',

    -- 特殊字段
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`      BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`       VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识（项目所属应用）',
    `LANGUAGE`    VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:h-project-2
ALTER TABLE E_PROJECT
    ADD UNIQUE (`CODE`, `SIGMA`);