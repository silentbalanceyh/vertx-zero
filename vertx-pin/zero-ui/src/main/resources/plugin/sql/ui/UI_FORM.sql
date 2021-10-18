-- liquibase formatted sql

-- changeset Lang:ox-form-1
-- 表单类：UI_FORM（Form使用主键命中，所以不考虑关联关系）
DROP TABLE IF EXISTS UI_FORM;
CREATE TABLE IF NOT EXISTS UI_FORM
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 表单名称',
    `CODE`       VARCHAR(255) COMMENT '「code」- 表单系统编码',
    `WINDOW`     DECIMAL(10, 2) COMMENT '「window」- window, Form对应的窗口配置',
    `COLUMNS`    INTEGER COMMENT '「columns」- columns, Form对应的配置',
    `HIDDEN`     TEXT COMMENT '「hidden」- 隐藏字段专用配置',
    `ROW`        TEXT COMMENT '「rowConfig/rowClass」- 行专用配置',

    -- 不使用名空间，直接和 sigma 绑定
    `IDENTIFIER` VARCHAR(255) COMMENT '「identifier」- 表单所属的模型ID',

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

-- changeset Lang:ox-form-2
ALTER TABLE UI_FORM
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;
