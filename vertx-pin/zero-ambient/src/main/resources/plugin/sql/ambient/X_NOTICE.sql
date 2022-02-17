-- liquibase formatted sql

-- changeset Lang:x-notice-1
-- 公告模块：X_NOTICE
DROP TABLE IF EXISTS X_NOTICE;
CREATE TABLE IF NOT EXISTS X_NOTICE
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 公告主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 公告标题',
    `CODE`       VARCHAR(255) COMMENT '「code」- 公告编码',
    `TYPE`       VARCHAR(255) COMMENT '「type」- 公告类型',

    `STATUS`     VARCHAR(255) COMMENT '「status」- 公告状态',
    `CONTENT`    LONGTEXT COMMENT '「content」- 公告内容',
    `EXPIRED_AT` DATETIME COMMENT '「createdAt」- 公告到期时间',

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

-- changeset Lang:x-notice-2
ALTER TABLE X_NOTICE
    ADD UNIQUE (`APP_ID`, `CODE`); -- 模板名称/编码
ALTER TABLE X_NOTICE
    ADD UNIQUE (`APP_ID`, `NAME`);