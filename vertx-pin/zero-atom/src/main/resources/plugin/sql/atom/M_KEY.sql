-- liquibase formatted sql

-- changeset Lang:ox-key-1
-- 模型表：M_KEY，键
DROP TABLE IF EXISTS M_KEY;
CREATE TABLE IF NOT EXISTS M_KEY
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 键ID',
    `NAME`       VARCHAR(255) COMMENT '「name」- 键名称',
    `TYPE`       VARCHAR(20) COMMENT '「type」- 键类型',
    `COLUMNS`    TEXT COMMENT '「columns」- JsonArray格式，键覆盖的列集合',
    `ENTITY_ID`  VARCHAR(36) COMMENT '「entityId」- 关联的实体ID',
    `COMMENTS`   TEXT COMMENT '「comments」- 当前属性的描述信息',

    -- 特殊字段
    `SIGMA`      VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`   VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`     BIT COMMENT '「active」- 是否启用',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-key-2
ALTER TABLE M_KEY
    ADD UNIQUE (`NAME`, `ENTITY_ID`) USING BTREE;

ALTER TABLE M_KEY
    ADD INDEX IDX_M_KEY_ENTITY_ID (`ENTITY_ID`) USING BTREE;