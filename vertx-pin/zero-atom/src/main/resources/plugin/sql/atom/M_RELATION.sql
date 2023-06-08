-- liquibase formatted sql

-- changeset Lang:ox-define-relation-1
-- 【定义】定义模型和模型之间的关系信息
DROP TABLE IF EXISTS M_RELATION;
CREATE TABLE IF NOT EXISTS M_RELATION
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 关系定义的主键',
    `TYPE`       VARCHAR(36) COMMENT '「type」- 关系类型 - 来自（字典）',
    /*
     *
     * 1）根据 upstream = 当前 读取；- 读取当前模型的下级
     * 2）根据 downstream = 当前 读取；- 读取当前模型的上级
     * 值节点为 JSON 格式，用于存储一些相关的配置信息，在业务场景中使用
     */
    `UPSTREAM`   VARCHAR(255) COMMENT '「upstream」- 当前关系是 upstream，表示上级',
    `DOWNSTREAM` VARCHAR(255) COMMENT '「downstream」- 当前关系是 downstream，表示下级',

    `COMMENTS`   TEXT COMMENT '「comments」- 关系定义的描述信息',

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
-- changeset Lang:ox-define-relation-2
ALTER TABLE M_RELATION
    ADD UNIQUE (`TYPE`, `UPSTREAM`, `DOWNSTREAM`, `SIGMA`) USING BTREE;
ALTER TABLE M_RELATION
    ADD INDEX (`UPSTREAM`, `DOWNSTREAM`, `SIGMA`) USING BTREE;