-- liquibase formatted sql

-- changeset Lang:x-integration-1
-- 集成专用表：X_INTEGRATION
DROP TABLE IF EXISTS X_INTEGRATION;
CREATE TABLE IF NOT EXISTS X_INTEGRATION
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 集成配置主键',
    `IP_V4`      VARCHAR(15) COMMENT '「ipV4」- IP v4地址',
    `IP_V6`      VARCHAR(40) COMMENT '「ipV6」- IP v6地址',
    `HOSTNAME`   VARCHAR(255) COMMENT '「hostname」- 主机地址',
    `PROTOCOL`   VARCHAR(64) COMMENT '「protocol」- 协议类型：HTTP, HTTPS, FTP',
    `ENDPOINT`   VARCHAR(255) COMMENT '「endpoint」- 端地址',
    `PATH`       VARCHAR(255) COMMENT '「path」- 集成专用根路径',

    `USERNAME`   VARCHAR(255) COMMENT '「username」- 账号',
    `PASSWORD`   VARCHAR(255) COMMENT '「password」- 密码',
    `PUBLIC_KEY` VARCHAR(255) COMMENT '「publicKey」- Key文件',

    `OPTIONS`    TEXT COMMENT '「options」- 集成相关配置',

    -- 特殊属性
    `APP_ID`     VARCHAR(255) COMMENT '「appId」- 关联的应用程序ID',

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

-- changeset Lang:x-integration-2
ALTER TABLE X_INTEGRATION
    ADD UNIQUE (`ENDPOINT`, `PATH`, `APP_ID`); -- 目前应用程序和数据源一对一，暂定