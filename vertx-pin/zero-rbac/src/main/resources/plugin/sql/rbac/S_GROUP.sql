-- liquibase formatted sql

-- changeset Lang:ox-group-1
-- 组专用权限：S_GROUP
DROP TABLE IF EXISTS S_GROUP;
CREATE TABLE IF NOT EXISTS S_GROUP
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 组ID',
    `NAME`       VARCHAR(255) COMMENT '「name」- 组名称',
    `CODE`       VARCHAR(255) COMMENT '「code」- 组系统码',
    `PARENT_ID`  VARCHAR(36) COMMENT '「parentId」- 父组ID（组支持树形结构，角色平行结构）',

    -- 模块相关 Join
    `MODEL_ID`   VARCHAR(255) COMMENT '「modelId」- 组所关联的模型identifier，用于描述',
    `MODEL_KEY`  VARCHAR(36) COMMENT '「modelKey」- 组所关联的模型记录ID，用于描述哪一个Model中的记录',

    -- 特殊字段
    `CATEGORY`   VARCHAR(36) COMMENT '「category」- 组类型',
    `SIGMA`      VARCHAR(128) COMMENT '「sigma」- 用户组绑定的统一标识',
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

-- changeset Lang:ox-group-2
-- Unique Key：独立唯一主键定义
ALTER TABLE S_GROUP
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;