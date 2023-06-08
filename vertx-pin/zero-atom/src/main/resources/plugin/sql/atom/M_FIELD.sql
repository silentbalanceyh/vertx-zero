-- liquibase formatted sql

-- changeset Lang:ox-field-1
-- 模型表：M_FIELD，字段
DROP TABLE IF EXISTS M_FIELD;
CREATE TABLE IF NOT EXISTS M_FIELD
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 字段ID',
    `NAME`          VARCHAR(255) COMMENT '「name」- 属性名（非列）',
    `TYPE`          VARCHAR(64) COMMENT '「type」- OX核心类型',
    `COLUMN_NAME`   VARCHAR(255) COMMENT '「columnName」- 数据库列名',
    `COLUMN_TYPE`   VARCHAR(255) COMMENT '「columnType」- 数据库转换过后的类型',

    -- 专用属性
    `IS_PRIMARY`    BIT COMMENT '「isPrimary」- 是否为主键', -- 特殊属性，用于解释当前Field是否为主键
    `IS_NULLABLE`   BIT COMMENT '「isNullable」- 是否可为空',
    `LENGTH`        INTEGER COMMENT '「length」- String类型的长度',
    `PRECISION`     INTEGER COMMENT '「precision」- Decimal类型的精度',
    `FORMAT`        VARCHAR(255) COMMENT '「format」- 当前数据列的格式，String或Date类型',
    `IN_COMPONENT`  VARCHAR(255) COMMENT '「inComponent」- 写入插件',
    `OUT_COMPONENT` VARCHAR(255) COMMENT '「outComponent」- 读取插件',
    `ENTITY_ID`     VARCHAR(36) COMMENT '「entityId」- 关联的实体ID',
    `COMMENTS`      TEXT COMMENT '「comments」- 当前属性的描述信息',

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

-- changeset Lang:ox-field-2
ALTER TABLE M_FIELD
    ADD UNIQUE (`NAME`, `ENTITY_ID`) USING BTREE;

ALTER TABLE M_FIELD
    ADD INDEX IDX_M_FIELD_ENTITY_ID (`ENTITY_ID`) USING BTREE;