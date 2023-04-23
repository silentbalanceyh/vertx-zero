-- liquibase formatted sql

-- changeset Lang:ox-suser-1
-- 用户专用表：S_USER
DROP TABLE IF EXISTS S_USER;
CREATE TABLE IF NOT EXISTS S_USER
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 用户ID',
    `USERNAME`   VARCHAR(255) COMMENT '「username」- 用户登录账号',
    `REALNAME`   VARCHAR(255) COMMENT '「realname」- 用户真实姓名',
    `ALIAS`      VARCHAR(255) COMMENT '「alias」- 用户昵称',
    `MOBILE`     VARCHAR(255) COMMENT '「mobile」- 用户登录手机',
    `EMAIL`      VARCHAR(255) COMMENT '「email」- 用户登录EMAIL地址',
    `PASSWORD`   VARCHAR(255) COMMENT '「password」- 用户登录密码',

    -- 模块相关 Join
    `MODEL_ID`   VARCHAR(255) COMMENT '「modelId」- 组所关联的模型identifier，用于描述',
    `MODEL_KEY`  VARCHAR(36) COMMENT '「modelKey」- 组所关联的模型记录ID，用于描述哪一个Model中的记录',

    -- 特殊字段
    `CATEGORY`   VARCHAR(36) COMMENT '「category」- 用户分类',
    `SIGMA`      VARCHAR(32) COMMENT '「sigma」- 用户绑定的统一标识',
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

-- changeset Lang:ox-suser-2
-- Unique Key: 独立唯一键定义
ALTER TABLE S_USER
    ADD UNIQUE (`USERNAME`, `SIGMA`) USING BTREE;
ALTER TABLE S_USER
    ADD INDEX IDX_S_USER_USERNAME (`USERNAME`) USING BTREE;
ALTER TABLE S_USER
    ADD INDEX IDX_S_USER_MODEL_KEY (`MODEL_KEY`) USING BTREE;