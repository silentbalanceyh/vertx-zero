-- liquibase formatted sql

-- changeset Lang:ox-menu-1
-- 系统菜单表：X_MENU
DROP TABLE IF EXISTS X_MENU;
CREATE TABLE IF NOT EXISTS X_MENU
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 菜单主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 菜单名称',
    `ICON`       VARCHAR(255) COMMENT '「icon」- 菜单使用的icon',
    `TEXT`       VARCHAR(255) COMMENT '「text」- 菜单显示文字',
    `URI`        VARCHAR(255) COMMENT '「uri」- 菜单地址（不包含应用的path）',
    `TYPE`       VARCHAR(255) COMMENT '「type」- 菜单类型',
    `ORDER`      BIGINT COMMENT '「order」- 菜单排序',
    `LEVEL`      BIGINT COMMENT '「level」- 菜单层级',

    `PARENT_ID`  VARCHAR(36) COMMENT '「parentId」- 菜单父ID',
    `APP_ID`     VARCHAR(36) COMMENT '「appId」- 应用程序ID',

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
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-menu-2
ALTER TABLE X_MENU
    ADD UNIQUE (`NAME`, `APP_ID`) USING BTREE;

-- 场景：读取应用中的菜单
ALTER TABLE X_MENU
    ADD INDEX IDX_X_MENU_APP_ID (`APP_ID`) USING BTREE;
ALTER TABLE X_MENU
    ADD INDEX IDX_X_MENU_APP_ID_SIGMA (`APP_ID`, `SIGMA`) USING BTREE;