-- liquibase formatted sql

-- changeset Lang:my-tpl-1
-- 个人应用表：MY_TPL
DROP TABLE IF EXISTS MY_TPL;
CREATE TABLE IF NOT EXISTS MY_TPL
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 个人设置主键',

    -- 关联关系
    `TPL_ID`     VARCHAR(36) COMMENT '「tplId」- 对应TPL的ID',
    `TPL_TYPE`   VARCHAR(36) COMMENT '「tplType」- 对应TPL类型',

    `OWNER`      VARCHAR(36) COMMENT '「owner」- 拥有者ID，我的 / 角色级',
    `OWNER_TYPE` VARCHAR(5) COMMENT '「ownerType」- ROLE 角色，USER 用户',
    -- 维度控制
    `TYPE`       VARCHAR(32) COMMENT '「type」- 类型（默认全站）',
    -- 特殊字段
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:my-tpl-2
ALTER TABLE MY_TPL
    ADD UNIQUE (`TYPE`, `TPL_TYPE`, `TPL_ID`, `OWNER`, `OWNER_TYPE`);