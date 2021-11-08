-- liquibase formatted sql

-- changeset Lang:w-instance-1
-- 流程定义表：W_INSTANCE
DROP TABLE IF EXISTS W_INSTANCE;
CREATE TABLE IF NOT EXISTS W_INSTANCE
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 流程定义主键',
    `CODE`             VARCHAR(255) COMMENT '「code」- 流程定义编号（系统可用）',
    `NAME`             VARCHAR(255) COMMENT '「name」- 流程标题',

    -- 父ID
    `INSTANCE_ID`      VARCHAR(64) NOT NULL COMMENT '「instanceId」- 实例ID（查询Task专用）：getProcessInstanceId',
    `INSTANCE_ROOT_ID` VARCHAR(64) COMMENT '「instanceRootId」- 根实例ID：getRootProcessInstanceId',
    `INSTANCE_CASE_ID` VARCHAR(64) COMMENT '「instanceCaseId」- getCaseInstanceId',
    `TENANT_ID`        VARCHAR(64) COMMENT '「tenantId」- 租户ID',

    -- 实例相关
    `P_DEFINITION_ID`  VARCHAR(64) COMMENT '「pDefinitionId」- 定义ID, getProcessDefinitionId',
    `P_EXECUTION_ID`   VARCHAR(64) COMMENT '「pExecutionId」- 任务执行ID（查询Task专用）- getId',
    `P_BUSINESS_KEY`   VARCHAR(64) COMMENT '「pBusinessId」- getBusinessKey',

    -- 特殊字段
    `ACTIVE`           BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`            VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`         TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`         VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`       DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`       VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`       DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`       VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:w-instance-2
ALTER TABLE W_INSTANCE
    ADD UNIQUE (`CODE`, `SIGMA`); -- 流程定义编码不重复
ALTER TABLE W_INSTANCE
    ADD UNIQUE (`INSTANCE_ID`, `SIGMA`); -- 流程定义编码不重复