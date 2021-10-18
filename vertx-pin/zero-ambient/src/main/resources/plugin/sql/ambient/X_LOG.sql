-- liquibase formatted sql

-- changeset Lang:ox-log-1
-- 应用程序表：X_LOG
DROP TABLE IF EXISTS X_LOG;
CREATE TABLE IF NOT EXISTS X_LOG
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 日志的主键',
    `TYPE`          VARCHAR(64) COMMENT '「type」- 日志的分类',
    `LEVEL`         VARCHAR(10) COMMENT '「level」- 日志级别：ERROR / WARN / INFO',

    -- 日志内容信息
    `INFO_STACK`    TEXT COMMENT '「infoStack」- 堆栈信息',
    `INFO_SYSTEM`   TEXT COMMENT '「infoSystem」- 日志内容',
    `INFO_READABLE` TEXT COMMENT '「infoReadable」- 日志的可读信息',
    `INFO_AT`       DATETIME COMMENT '「infoAt」- 日志记录时间',

    -- 日志扩展信息
    `LOG_AGENT`     VARCHAR(255) COMMENT '「logAgent」- 记录日志的 agent 信息',
    `LOG_IP`        VARCHAR(255) COMMENT '「logIp」- 日志扩展组件',
    `LOG_USER`      VARCHAR(36) COMMENT '「logUser」- 日志记录人',

    -- 特殊字段
    `ACTIVE`        BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`         VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`      VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- 日志管理
ALTER TABLE X_LOG
    ADD INDEX IDXM_X_LOG_SIGMA_TYPE (`SIGMA`, `TYPE`) USING BTREE;