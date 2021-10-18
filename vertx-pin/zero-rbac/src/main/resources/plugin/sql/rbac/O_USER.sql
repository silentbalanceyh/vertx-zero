-- liquibase formatted sql

-- changeset Lang:ox-ouser-1
-- OAuth认证表：O_USER
-- 暂时考虑将Token存储在内存，所以临时授权码、Token都需要通过程序来实现，仅保存用户本身的Token信息就好
DROP TABLE IF EXISTS O_USER;
CREATE TABLE IF NOT EXISTS O_USER
(
    `KEY`           VARCHAR(36) COMMENT '「key」- OAuth用户ID',
    `REDIRECT_URI`  TEXT COMMENT '「redirectUri」- 回调重定向地址',
    `CODE`          VARCHAR(36) COMMENT '「code」- 系统编号',
    `CLIENT_SECRET` VARCHAR(64) COMMENT '「clientSecret」- 客户端密钥',
    `CLIENT_ID`     VARCHAR(36) COMMENT '「clientId」- 客户端ID',
    `GRANT_TYPE`    VARCHAR(32) COMMENT '「grantType」- 认证方式',
    `SCOPE`         VARCHAR(64) COMMENT '「scope」- 对应名空间，以应用为中心',
    `STATE`         VARCHAR(128) COMMENT '「state」- 客户端状态',
    -- 特殊字段
    `LANGUAGE`      VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`        BIT COMMENT '「active」- 是否启用',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-ouser-2
-- Unique Key
ALTER TABLE O_USER
    ADD UNIQUE (`CLIENT_ID`) USING BTREE;
ALTER TABLE O_USER
    ADD UNIQUE (`CLIENT_SECRET`) USING BTREE;

-- 生成临时授权码：/oauth/authorize
ALTER TABLE O_USER
    ADD INDEX IDXM_O_USER_OAUTH_AUTHORIZE_APP (`SCOPE`, `CLIENT_ID`, `CLIENT_SECRET`) USING BTREE;
ALTER TABLE O_USER
    ADD INDEX IDXM_O_USER_OAUTH_AUTHORIZE_FULL (`SCOPE`, `CLIENT_ID`, `CLIENT_SECRET`, `LANGUAGE`) USING BTREE;
