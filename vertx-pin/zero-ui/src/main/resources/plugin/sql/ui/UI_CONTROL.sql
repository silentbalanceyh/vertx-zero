-- liquibase formatted sql

-- changeset Lang:ox-control-1
-- 控件表：X_CONTROL
DROP TABLE IF EXISTS UI_CONTROL;
CREATE TABLE IF NOT EXISTS UI_CONTROL
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 主键',
    `SIGN`             VARCHAR(64) COMMENT '「sign」- 控件使用的签名基本信息',
    `PAGE_ID`          VARCHAR(36) COMMENT '「pageId」- 当前控件所在的页面ID',
    `TYPE`             VARCHAR(32) COMMENT '「type」- 当前控件的类型：CONTAINER / COMPONENT / FORM / LIST，其中 FORM / LIST 需要访问子表',

    -- UI布局基本配置
    /*
     * 控件本身带容器的时候使用
     * containerName
     * containerConfig
     */
    `CONTAINER_NAME`   VARCHAR(64) COMMENT '「containerName」- 当前控件使用的容器名',
    `CONTAINER_CONFIG` TEXT COMMENT '「containerConfig」- 当前控件使用的容器配置',

    -- type = CONTAINER 的配置
    `ASSIST`           TEXT COMMENT '「assist」 - 辅助数据（容器专用）',
    `GRID`             TEXT COMMENT '「grid」 - 容器专用',

    -- type = COMPONENT 的配置
    `COMPONENT_NAME`   VARCHAR(64) COMMENT '「componentName」- 当前控件使用的组件名',
    `COMPONENT_CONFIG` TEXT COMMENT '「componentConfig」- 当前控件使用的配置',
    `COMPONENT_DATA`   VARCHAR(255) COMMENT '「componentData」- 当前控件使用的数据，使用表达式结构',
    /*
     * type = LIST / FORM 的配置（无其他配置，直接使用 controlId 读取
     * 1) type = LIST，读取：UI_LIST + UI_COLUMN + UI_OP
     * 2) type = FORM, 读取：UI_FORM + UI_FIELD + UI_OP
     */

    -- 特殊字段
    `ACTIVE`           BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`            VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`         TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`         VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`       DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`       VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`       DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`       VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-control-2
ALTER TABLE UI_CONTROL
    ADD UNIQUE (`SIGN`) USING BTREE; -- 控件签名全局唯一