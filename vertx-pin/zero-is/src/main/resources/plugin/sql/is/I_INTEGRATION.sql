-- liquibase formatted sql

-- changeset Lang:i-integration-1
-- 集成专用表：I_INTEGRATION
DROP TABLE IF EXISTS I_INTEGRATION;
CREATE TABLE IF NOT EXISTS I_INTEGRATION
(
    -- 基本信息
    `KEY`             VARCHAR(36) COMMENT '「key」- 集成配置主键',
    `NAME`            VARCHAR(255) COMMENT '「name」- 集成名称',
    `TYPE`            VARCHAR(255) COMMENT '「type」- 集成类型',

    -- 网络信息
    `IP_V4`           VARCHAR(15) COMMENT '「ipV4」- IP v4地址',
    `IP_V6`           VARCHAR(40) COMMENT '「ipV6」- IP v6地址',
    `HOSTNAME`        VARCHAR(255) COMMENT '「hostname」- 主机地址',

    `PORT`            INT COMMENT '「port」- 端口号',
    `PROTOCOL`        VARCHAR(64) COMMENT '「protocol」- 协议类型：HTTP, HTTPS, FTP',

    -- 特殊协议的安全相关信息
    `SECURE_PORT`     INT COMMENT '「securePort」- 传输层安全接口',
    `SECURE_PROTOCOL` VARCHAR(32) COMMENT '「secureProtocol」- 传入层协议：TLS / SSL（邮件服务器需要）',

    `ENDPOINT`        VARCHAR(255) COMMENT '「endpoint」- 端地址',
    `PATH`            VARCHAR(255) COMMENT '「path」- 集成专用根路径',

    `OS_KEY`          NVARCHAR(1024) COMMENT '「osKey」- 开源专用Key',
    `OS_SECRET`       NVARCHAR(1024) COMMENT '「osSecret」- 开源专用Secret',
    `OS_AUTHORIZE`    VARCHAR(255) COMMENT '「osAuthorize」- Authorize接口',
    `OS_TOKEN`        VARCHAR(255) COMMENT '「osToken」- Token接口',

    `USERNAME`        VARCHAR(255) COMMENT '「username」- 账号',
    `PASSWORD`        VARCHAR(255) COMMENT '「password」- 密码',
    `PUBLIC_KEY`      VARCHAR(255) COMMENT '「publicKey」- Key文件',

    `OPTIONS`         TEXT COMMENT '「options」- 集成相关配置',

    -- 特殊属性
    `APP_ID`          VARCHAR(255) COMMENT '「appId」- 关联的应用程序ID',

    -- 特殊字段
    `ACTIVE`          BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`           VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`        VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:i-integration-2
ALTER TABLE I_INTEGRATION
    ADD UNIQUE (`ENDPOINT`, `PATH`, `APP_ID`); -- 目前应用程序和数据源一对一，暂定