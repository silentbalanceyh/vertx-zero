-- liquibase formatted sql

-- changeset Lang:ox-module-1
-- 应用程序中的模块表：X_MODULE
DROP TABLE IF EXISTS X_MODULE;
CREATE TABLE IF NOT EXISTS X_MODULE
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 模块唯一主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 模块名称',
    `CODE`       VARCHAR(36) COMMENT '「code」- 模块编码',
    `ENTRY`      VARCHAR(255) COMMENT '「entry」— 模块入口地址',
    `BLOCK_CODE` VARCHAR(255) COMMENT '「blockCode」— 所属模块系统编码',

    `APP_ID`     VARCHAR(255) COMMENT '「appId」- 关联的应用程序ID', -- 一对多，一个App下会包含多个Module
    `MODEL_ID`   VARCHAR(36) COMMENT '「modelId」- 当前模块关联的主模型ID',

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
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-module-2
ALTER TABLE X_MODULE
    ADD UNIQUE (`ENTRY`, `APP_ID`) USING BTREE; -- 页面入口地址