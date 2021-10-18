-- liquibase formatted sql

-- changeset Lang:ox-acc-1
-- 增量表：ACC - Accumulative
DROP TABLE IF EXISTS M_ACC;
CREATE TABLE IF NOT EXISTS M_ACC
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 增量记录ID',

    -- 关联模型专用记录
    `MODEL_ID`      VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`     VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `RECORD_JSON`   LONGTEXT COMMENT '「recordJson」- 上一次的记录内容（Json格式）',
    `RECORD_RAW`    LONGTEXT COMMENT '「recordRaw」- 上一次的记录的原始内容',
    `RECORD_UNIQUE` VARCHAR(255) COMMENT '「recordUnique」- 业务标识规则专用Key，可计算获取',

    -- 特殊字段
    `SIGMA`         VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`      VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`        BIT COMMENT '「active」- 是否启用',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:ox-acc-2
ALTER TABLE M_ACC
    ADD UNIQUE (`SIGMA`, `MODEL_KEY`) USING BTREE;