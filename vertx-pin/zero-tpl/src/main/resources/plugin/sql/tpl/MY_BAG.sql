-- liquibase formatted sql

-- changeset Lang:my-bag-1
-- 个人应用表：MY_BAG
DROP TABLE IF EXISTS MY_BAG;
CREATE TABLE IF NOT EXISTS MY_BAG
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 个人应用主键',
    `BAG_ID`     VARCHAR(36) COMMENT '「bagId」- 个人包主键',

    `OWNER`      VARCHAR(36) COMMENT '「owner」- 拥有者ID，我的 / 角色级',
    `OWNER_TYPE` VARCHAR(5) COMMENT '「ownerType」- ROLE 角色，USER 用户',

    -- UI定制
    `UI_SORT`    BIGINT COMMENT '「uiSort」- 模块排序',

    -- 维度控制
    `TYPE`       VARCHAR(32) COMMENT '「type」- 类型（默认全站）',
    `POSITION`   VARCHAR(16) COMMENT '「position」- 位置（默认左上）',

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

-- changeset Lang:my-bag-2
ALTER TABLE MY_BAG
    ADD UNIQUE (`OWNER_TYPE`, `OWNER`, `TYPE`, `POSITION`, `BAG_ID`);