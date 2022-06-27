-- liquibase formatted sql

-- changeset Lang:tpl-ticket-1
-- 模板表：TPL_TICKET
DROP TABLE IF EXISTS TPL_TICKET;
CREATE TABLE IF NOT EXISTS TPL_TICKET
(
    `KEY`              VARCHAR(36) NOT NULL COMMENT '「key」- 增量记录ID',
    `CODE`             VARCHAR(50) NOT NULL COMMENT '「code」- 编码',
    `NAME`             VARCHAR(50)  DEFAULT NULL COMMENT '「name」- 名称',
    `DESCRIPTION`      VARCHAR(255) DEFAULT NULL COMMENT '「description」- 描述',
    `TYPE`             VARCHAR(36)  DEFAULT NULL COMMENT '「type」- 分类',
    `STATUS`           VARCHAR(36)  DEFAULT NULL COMMENT '「status」- 状态',
    `SYSTEM`           BIT(1)       DEFAULT NULL COMMENT '「system」- 是否属于系统模板',

    -- Model
    `MODEL_ID`         VARCHAR(255) DEFAULT NULL COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`        VARCHAR(36)  DEFAULT NULL COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `MODEL_CATEGORY`   VARCHAR(36)  DEFAULT NULL COMMENT '「modelCategory」- 模型分类',
    -- Record of Data
    `RECORD_JSON`      LONGTEXT COMMENT '「recordJson」- 上一次的记录内容（Json格式）',
    `RECORD_COMPONENT` VARCHAR(255) COMMENT '「recordComponent」- 处理记录的组件',
    -- Ui Config
    `UI_CONFIG`        LONGTEXT COMMENT '「uiConfig」- UI的配置（Json格式）',
    `UI_COMPONENT`     VARCHAR(255) COMMENT '「uiComponent」- 处理UI的组件',

    -- 特殊字段
    `SIGMA`            VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`         VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`           BIT COMMENT '「active」- 是否启用',
    `METADATA`         TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`       DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`       VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`       DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`       VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:tpl-ticket-2
ALTER TABLE TPL_TICKET
    ADD UNIQUE (`CODE`, `SIGMA`);