-- liquibase formatted sql

-- changeset Lang:w-flow-1
-- 流程定义表：W_FLOW
DROP TABLE IF EXISTS W_FLOW;
CREATE TABLE IF NOT EXISTS W_FLOW
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 流程定义主键',
    `NAME`           VARCHAR(255) COMMENT '「name」- 流程定义名称',
    `CODE`           VARCHAR(255) COMMENT '「code」- 流程定义编号（系统可用）',
    `TYPE`           VARCHAR(36) COMMENT '「type」- 流程类型，对接 zero.workflow.type的X_TABULAR',

    -- 关联Camunda读取流程图
    `DEFINITION_ID`  VARCHAR(64) COMMENT '「definitionId」- 定义ID（读取流程图所需）, getProcessDefinitionId',

    `RUN_COMPONENT`  VARCHAR(255) COMMENT '「runComponent」- 执行组件',
    `RUN_CONFIG`     TEXT COMMENT '「runConfig」- 执行配置',

    `BOOT_COMPONENT` VARCHAR(255) COMMENT '「bootComponent」- 启动组件',
    `BOOT_CONFIG`    TEXT COMMENT '「bootConfig」- 启动配置',

    -- Todo Configuration
    `TODO_COMPONENT` VARCHAR(255) COMMENT '「todoComponent」- Todo专用组件',
    `TODO_CONFIG`    VARCHAR(255) COMMENT '「todoConfig」- Todo配置，生成UI专用配置（旧系统所需，新系统会扩展此处的写法）',

    -- 特殊字段
    `COMMENT`        LONGTEXT COMMENT '「comment」 - 流程定义备注',
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:w-flow-2
ALTER TABLE W_FLOW
    ADD UNIQUE (`NAME`, `SIGMA`); -- 流程定义名称不重复
ALTER TABLE W_FLOW
    ADD UNIQUE (`CODE`, `SIGMA`); -- 流程定义编码不重复