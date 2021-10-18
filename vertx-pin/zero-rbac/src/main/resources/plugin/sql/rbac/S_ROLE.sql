-- liquibase formatted sql

-- changeset Lang:ox-role-1
-- 角色专用表：S_ROLE
DROP TABLE IF EXISTS S_ROLE;
CREATE TABLE IF NOT EXISTS S_ROLE
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 角色ID',
    `NAME`       VARCHAR(255) COMMENT '「name」- 角色名称',
    `CODE`       VARCHAR(255) COMMENT '「code」- 角色系统名',
    `POWER`      BIT COMMENT '「power」- 是否具有定制权限？',
    `COMMENT`    TEXT COMMENT '「comment」- 角色备注信息',

    -- 模块相关 Join
    `MODEL_ID`   VARCHAR(255) COMMENT '「modelId」- 组所关联的模型identifier，用于描述',
    `MODEL_KEY`  VARCHAR(36) COMMENT '「modelKey」- 组所关联的模型记录ID，用于描述哪一个Model中的记录',

    -- 特殊字段
    `CATEGORY`   VARCHAR(36) COMMENT '「category」- 组类型',
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
-- changeset Lang:ox-role-2
-- Unique Key：独立唯一键定义
ALTER TABLE S_ROLE
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;