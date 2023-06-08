-- liquibase formatted sql

-- changeset Lang:ox-column-1
-- 字段表：UI_COLUMN，列表专用，CONTROL中的 type = LIST 类型
DROP TABLE IF EXISTS UI_COLUMN;
CREATE TABLE IF NOT EXISTS UI_COLUMN
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 列主键',
    `TITLE`         VARCHAR(255) COMMENT '「title」- 列标题',
    `DATA_INDEX`    VARCHAR(255) COMMENT '「dataIndex」- 列名',
    `POSITION`      INT COMMENT '「position」- 当前列的位置',
    `RENDER`        VARCHAR(64) COMMENT '「render」- 使用的Render函数',
    /*
     * 每一列的详细配置信息
     */
    -- 强制宽度
    `WIDTH`         INTEGER COMMENT '「width」- 当前列的宽度',
    `FIXED`         BIT         DEFAULT FALSE COMMENT '「fixed」- 当前列是否固定',
    `CLASS_NAME`    VARCHAR(255) COMMENT '「className」- 当前列的特殊CSS类',

    -- 排序/过滤
    `SORTER`        BIT         DEFAULT FALSE COMMENT '「sorter」- 当前列是否支持排序',
    `FILTER_TYPE`   VARCHAR(10) COMMENT '「filterType」- $filter.type 支持列搜索时的搜索类型',
    `FILTER_CONFIG` TEXT COMMENT '「filterConfig」- $filter.config 列搜索支持时的搜索配置',

    -- Zero 专用配置
    `EMPTY`         VARCHAR(64) COMMENT '「empty」- $empty 专用',
    `MAPPING`       TEXT COMMENT '「mapping」- $mapping专用',
    `CONFIG`        TEXT COMMENT '「config」- $config专用',
    `OPTION`        TEXT COMMENT '「option」- $option专用，executor时',
    `FORMAT`        VARCHAR(128) COMMENT '「format」- $format时间格式专用',
    `DATUM`         TEXT COMMENT '「datum」- $datum专用', -- 双格式

    /*
     * CONTROL_ID 主要包含两种
     * 1）GLOBAL的，如果是 identifier 的值则表示当前模型的全列
     * 2）UUID格式，根据单个 control 配置的值来表示
     */
    `CONTROL_ID`    VARCHAR(36) COMMENT '「controlId」- 关联的控件ID',

    -- 特殊字段
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

-- changeset Lang:ox-column-2
ALTER TABLE UI_COLUMN
    ADD UNIQUE (`SIGMA`, `CONTROL_ID`, `DATA_INDEX`) USING BTREE;

ALTER TABLE UI_COLUMN
    ADD INDEX IDXM_UI_COLUMN_SIGMA_CONTROL_ID (`SIGMA`, `CONTROL_ID`) USING BTREE;
ALTER TABLE UI_COLUMN
    ADD INDEX IDXM_UI_COLUMN_DATA_INDEX_CONTROL_ID (`DATA_INDEX`, `CONTROL_ID`) USING BTREE;