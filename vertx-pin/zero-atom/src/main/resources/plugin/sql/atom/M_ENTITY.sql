-- liquibase formatted sql

-- changeset Lang:ox-entity-1
-- 建模处理中的实体表：M_ENTITY
DROP TABLE IF EXISTS M_ENTITY;
CREATE TABLE IF NOT EXISTS M_ENTITY
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 实体ID',
    `IDENTIFIER` VARCHAR(255) COMMENT '「identifier」- 当前实体全局唯一ID',
    `NAMESPACE`  VARCHAR(255) COMMENT '「namespace」- 当前实体使用的名空间',
    `NAME`       VARCHAR(255) COMMENT '「name」- 当前实体的名称',
    `TYPE`       VARCHAR(20) COMMENT '「type」- 实体类型：ENTITY/RELATION',
    `TABLE_NAME` VARCHAR(255) COMMENT '「tableName」- 实体对应的数据库表',
    `COMMENTS`   TEXT COMMENT '「comments」- 数据库表备注',

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

-- changeset Lang:ox-entity-2
ALTER TABLE M_ENTITY
    ADD UNIQUE (`NAMESPACE`, `IDENTIFIER`) USING BTREE;
ALTER TABLE M_ENTITY
    ADD UNIQUE (`SIGMA`, `TABLE_NAME`) USING BTREE; -- 防止出现表混用（表名必须唯一，理论上不同企业应该分库）
ALTER TABLE M_ENTITY
    ADD UNIQUE (`NAMESPACE`, `NAME`) USING BTREE;