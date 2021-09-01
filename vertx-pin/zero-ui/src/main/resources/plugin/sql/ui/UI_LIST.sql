-- liquibase formatted sql

-- changeset Lang:ox-list-1
-- 表单类：UI_LIST
DROP TABLE IF EXISTS UI_LIST;
CREATE TABLE IF NOT EXISTS UI_LIST
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 主键',
    `NAME`           VARCHAR(255) COMMENT '「name」- 名称',
    `CODE`           VARCHAR(255) COMMENT '「code」- 系统编码',

    -- 不使用名空间，直接和 sigma 绑定
    `IDENTIFIER`     VARCHAR(255) COMMENT '「identifier」- 表单所属的模型ID',

    `V_QUERY`        VARCHAR(36) COMMENT '「vQuery」- 连接query到 grid -> query 节点',
    `V_SEARCH`       VARCHAR(36) COMMENT '「vSearch」- 连接search到 grid -> options 节点',
    `V_TABLE`        VARCHAR(36) COMMENT '「vTable」- 连接table到 grid -> table 节点',
    `V_SEGMENT`      TEXT COMMENT '「vSegment」- Json结构，对应到 grid -> component 节点',

    `DYNAMIC_COLUMN` BIT COMMENT '「dynamicColumn」- 动态列？',
    `DYNAMIC_SWITCH` BIT COMMENT '「dynamicSwitch」- 动态切换？',

    `OPTIONS_AJAX`   TEXT COMMENT '「optionsAjax」- 所有 ajax系列的配置',
    `OPTIONS_SUBMIT` TEXT COMMENT '「optionsSubmit」- 所有提交类的配置',
    `OPTIONS`        TEXT COMMENT '「options」- 配置项',

    -- 特殊插件（用于执行一些特殊组装和转换，读取到UI配置过后，通过组装器组装成最终配置信息）
    `CLASS_COMBINER` VARCHAR(255) COMMENT '「classCombiner」- 组装器',

    -- 特殊字段
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:ox-list-2
ALTER TABLE UI_LIST
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;