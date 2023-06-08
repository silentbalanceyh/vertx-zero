-- liquibase formatted sql

-- changeset Lang:ox-access-token-1
-- OAuth令牌表：O_ACCESS_TOKEN
-- 令牌需要存储在数据库中
DROP TABLE IF EXISTS O_ACCESS_TOKEN;
CREATE TABLE IF NOT EXISTS O_ACCESS_TOKEN
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 令牌主键',
    `TOKEN`         BLOB COMMENT '「token」- 用户的Token信息',
    `AUTH`          BLOB COMMENT '「auth」- 用户的ID的 byte[] 信息',
    `EXPIRED_TIME`  BIGINT COMMENT '「expiredTime」- 用户的Token过期时间',
    `REFRESH_TOKEN` BLOB COMMENT '「refreshToken」- 用户的刷新令牌',

    -- 特殊字段
    `LANGUAGE`      VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`        BIT COMMENT '「active」- 是否启用',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    PRIMARY KEY (`KEY`) USING BTREE
);