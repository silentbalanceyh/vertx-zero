-- liquibase formatted sql

-- changeset Lang:ox-permission-1
-- 权限专用表：S_PERMISSION
DROP TABLE IF EXISTS S_PERMISSION;
CREATE TABLE IF NOT EXISTS S_PERMISSION
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 权限ID',
    `NAME`       VARCHAR(255) COMMENT '「name」- 权限名称',
    `CODE`       VARCHAR(255) COMMENT '「code」- 权限系统码',
    -- 权限属性
    `IDENTIFIER` VARCHAR(255) COMMENT '「identifier」- 当前权限所属的Model的标识',

    -- 特殊字段
    `SIGMA`      VARCHAR(128) COMMENT '「sigma」- 绑定的统一标识',
    `LANGUAGE`   VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`     BIT COMMENT '「active」- 是否启用',
    `COMMENT`    TEXT COMMENT '「comment」- 权限说明',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-permission-2
-- Unique Key：独立唯一键定义
ALTER TABLE S_PERMISSION
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;