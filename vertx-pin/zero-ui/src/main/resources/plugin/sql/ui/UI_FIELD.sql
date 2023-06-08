-- liquibase formatted sql

-- changeset Lang:ox-field-1
-- 字段表：UI_FIELD，表单专用，CONTROL中的 type = FORM 类型
DROP TABLE IF EXISTS UI_FIELD;
CREATE TABLE IF NOT EXISTS UI_FIELD
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 字段主键',
    -- 构造 Form 中的UI专用
    `X_POINT`       INTEGER COMMENT '「xPoint] - 字段的X坐标（列）',
    `Y_POINT`       INTEGER COMMENT '「yPoint」- 字段的Y坐标（行）',

    -- Form 中的基本配置
    `LABEL`         VARCHAR(255) COMMENT '「label」- 字段标签',
    `NAME`          VARCHAR(255) COMMENT '「name」- 字段名称',
    `SPAN`          INTEGER COMMENT '「span」- 字段宽度',
    `HIDDEN`        BIT COMMENT '「hidden」- button专用',
    `RENDER`        VARCHAR(64) COMMENT '「render」- 使用的Render函数',

    -- Form 中的容器字段相关配置
    `CONTAINER`     VARCHAR(128) COMMENT '「container」- 容器字段专用容器信息，映射到 name 中',

    -- Option选项
    `OPTION_JSX`    TEXT COMMENT '「optionJsx」- 字段专用配置',
    `OPTION_CONFIG` TEXT COMMENT '「optionConfig」- 字段专用配置',
    `OPTION_ITEM`   TEXT COMMENT '「optionItem」- 字段专用配置',
    `RULES`         TEXT COMMENT '「rules」- optionConfig.rules 验证专用的配置，描述规则',
    -- 系统数据
    `CONTROL_ID`    VARCHAR(36) COMMENT '「controlId」- 关联的表单ID',

    -- 特殊字段
    `ROW_TYPE`      VARCHAR(20) DEFAULT NULL COMMENT '「rowType」- 行类型',
    `ACTIVE`        BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`         VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`      VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:ox-field-2
ALTER TABLE UI_FIELD
    ADD UNIQUE (`CONTROL_ID`, `NAME`) USING BTREE;

ALTER TABLE UI_FIELD
    ADD INDEX IDX_UI_FIELD_CONTROL_ID (`CONTROL_ID`) USING BTREE;
ALTER TABLE UI_FIELD
    ADD INDEX IDXM_UI_FIELD_CONTROL_ID_X_Y (`CONTROL_ID`, `X_POINT`, `Y_POINT`) USING BTREE;