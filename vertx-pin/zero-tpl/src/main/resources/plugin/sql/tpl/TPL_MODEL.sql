-- liquibase formatted sql

-- changeset Lang:tpl-model-1
-- 模板表：TPL_MODEL
DROP TABLE IF EXISTS TPL_MODEL;
CREATE TABLE IF NOT EXISTS TPL_MODEL
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 模板ID',
    `NAME`            VARCHAR(256) COMMENT '「name」- 模板名称',
    `CODE`            VARCHAR(256) COMMENT '「code」- 模板编码',
    `TYPE`            VARCHAR(36) COMMENT '「type」- 模板类型',

    -- 系统配置
    `TPL_CATEGORY`    LONGTEXT COMMENT '「tplCategory」- 分类配置',
    `TPL_INTEGRATION` LONGTEXT COMMENT '「tplIntegration」- 集成配置',
    `TPL_ACL`         LONGTEXT COMMENT '「tplAcl」- 安全配置',
    `TPL_ACL_VISIT`   LONGTEXT COMMENT '「tplAclVisit」- 资源访问者配置',

    -- 基础配置
    `TPL_MODEL`       LONGTEXT COMMENT '「tplModel」- 模型配置',
    `TPL_ENTITY`      LONGTEXT COMMENT '「tplEntity」- 实体配置',

    -- 接口任务配置
    `TPL_API`         LONGTEXT COMMENT '「tplApi」- 接口配置',
    `TPL_JOB`         LONGTEXT COMMENT '「tplJob」- 任务配置',

    -- 界面配置
    `TPL_UI`          LONGTEXT COMMENT '「tplUi」- 界面配置',
    `TPL_UI_LIST`     LONGTEXT COMMENT '「tplUiList」- 界面列表配置',
    `TPL_UI_FORM`     LONGTEXT COMMENT '「tplUiForm」- 界面表单配置',

    -- 特殊字段
    `SIGMA`           VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`        VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`          BIT COMMENT '「active」- 是否启用',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:tpl-model-2
ALTER TABLE TPL_MODEL
    ADD UNIQUE (`CODE`, `SIGMA`);