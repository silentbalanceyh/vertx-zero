-- liquibase formatted sql

-- changeset Lang:my-desktop-1
-- 个人应用表：MY_DESKTOP
DROP TABLE IF EXISTS MY_DESKTOP;
CREATE TABLE IF NOT EXISTS MY_DESKTOP
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 个人工作台',

    `BAG_ID`     VARCHAR(36) COMMENT '「bagId」- 所属个人应用',
    -- 模块核心配置
    `UI_CONFIG`  LONGTEXT COMMENT '「uiConfig」- 看板专用配置',
    `UI_GRID`    LONGTEXT COMMENT '「uiGrid」- 看板布局配置',

    `OWNER`      VARCHAR(128) COMMENT '「owner」- 拥有者ID，我的 / 角色级',
    `OWNER_TYPE` VARCHAR(5) COMMENT '「ownerType」- ROLE 角色，USER 用户',

    -- 特殊字段
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
)