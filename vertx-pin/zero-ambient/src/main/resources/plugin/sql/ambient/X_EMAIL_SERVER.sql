-- liquibase formatted sql

-- changeset Lang:x-email-server-1
-- 邮件服务器专用表：X_EMAIL_SERVER
DROP TABLE IF EXISTS X_EMAIL_SERVER;
CREATE TABLE IF NOT EXISTS X_EMAIL_SERVER
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 邮件服务器主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 邮件服务器名称',
    `IP_V4`      VARCHAR(15) COMMENT '「ipV4」- IP v4地址',
    `IP_V6`      VARCHAR(40) COMMENT '「ipV6」- IP v6地址',
    `HOSTNAME`   VARCHAR(255) COMMENT '「hostname」- 主机地址',
    `PORT`       INTEGER COMMENT '「port」- 端口号',
    `PROTOCOL`   VARCHAR(64) COMMENT '「protocol」协议类型,POP3, STMP 等',

    `SENDER`     VARCHAR(255) COMMENT '「sender」- 发送者账号',
    `PASSWORD`   VARCHAR(255) COMMENT '「password」- 口令',
    `OPTIONS`    TEXT COMMENT '「options」- 连接字符串中的配置key=value',

    -- 特殊属性
    `APP_ID`     VARCHAR(36) COMMENT '「appId」- 所属应用ID',

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

-- changeset Lang:x-email-server-2
ALTER TABLE X_EMAIL_SERVER
    ADD UNIQUE (`APP_ID`, `IP_V4`, `PORT`); -- 邮件服务器IP + PORT
ALTER TABLE X_EMAIL_SERVER
    ADD UNIQUE (`APP_ID`, `IP_V6`, `PORT`); -- 邮件服务器IP + PORT