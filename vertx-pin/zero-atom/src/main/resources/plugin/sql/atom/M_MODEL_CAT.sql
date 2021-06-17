-- liquibase formatted sql

-- changeset Lang:ox-modeldef-1
-- 模型定义表：包含分类信息
DROP TABLE IF EXISTS M_MODEL_CAT;
CREATE TABLE IF NOT EXISTS M_MODEL_CAT
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 分类ID',
    `NAME`       VARCHAR(255) COMMENT '「name」 - 定义名称，不可重复，位于模型分类管理顶层',

    -- 模型定义相关数据信息
    `CAT_NAME`   VARCHAR(255) COMMENT '「catName」 - 分类别名',

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
    PRIMARY KEY (`KEY`)
)
