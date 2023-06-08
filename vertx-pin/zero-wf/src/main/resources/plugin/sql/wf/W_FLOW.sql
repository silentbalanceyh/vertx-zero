-- liquibase formatted sql

-- changeset Lang:w-flow-1
-- 流程定义表：W_FLOW
DROP TABLE IF EXISTS W_FLOW;
CREATE TABLE IF NOT EXISTS W_FLOW
(
    `KEY`                  VARCHAR(36) COMMENT '「key」- 流程定义主键',
    `NAME`                 VARCHAR(255) COMMENT '「name」- 流程定义名称',
    `CODE`                 VARCHAR(255) COMMENT '「code」- 流程定义编号（系统可用）',
    `TYPE`                 VARCHAR(36) COMMENT '「type」- 流程类型，对接 zero.workflow.cat的X_TABULAR',

    -- 关联Camunda读取流程图
    `DEFINITION_KEY`       VARCHAR(128) COMMENT '「definitionKey」- 定义ID（读取流程图所需）, getProcessDefinitionId',

    -- 读取流程授权专用组件以及配置
    `AUTHORIZED_COMPONENT` VARCHAR(255) COMMENT '「authorizedComponent」- 流程授权组件',
    `AUTHORIZED_CONFIG`    LONGTEXT COMMENT '「authorizedConfig」- 流程授权配置',

    -- Todo生成组件
    `GENERATE_COMPONENT`   VARCHAR(255) COMMENT '「generateComponent」- Todo生成组件',
    `GENERATE_CONFIG`      LONGTEXT COMMENT '「generateConfig」- Todo生成配置',

    -- Todo执行，该组件执行后，会生成新的TODO
    `RUN_COMPONENT`        VARCHAR(255) COMMENT '「runComponent」- 执行组件',
    `RUN_CONFIG`           LONGTEXT COMMENT '「runConfig」- 执行配置',

    -- 仅用于第一个节点的流程组件
    `START_COMPONENT`      VARCHAR(255) COMMENT '「startComponent」- 启动组件',
    `START_CONFIG`         LONGTEXT COMMENT '「startConfig」- 启动配置',

    -- 最后一个节点的Callback
    `END_COMPONENT`        VARCHAR(255) COMMENT '「endComponent」- 完成组件',
    `END_CONFIG`           LONGTEXT COMMENT '「endConfig」- 完成配置',

    /*
     * 界面组件配置中
     * - uiComponent 为保留配置，后期扩展用
     * - uiConfig 为标准化的界面配置
     * - uiAssist 为当前流程容器专用辅助数据配置，一个界面提供给所有节点使用
     * - uiLinkage 为该流程中关联部分相关配置
     */
    `UI_COMPONENT`         VARCHAR(255) COMMENT '「uiComponent」- 界面组件',
    `UI_CONFIG`            LONGTEXT COMMENT '「uiConfig」- 界面配置',
    `UI_ASSIST`            LONGTEXT COMMENT '「uiAssist」- 界面辅助数据专用配置',
    `UI_LINKAGE`           LONGTEXT COMMENT '「uiLinkage」- 关联部分专用配置：关联工单、关联资产、关联附件',

    -- 特殊字段
    `COMMENT`              LONGTEXT COMMENT '「comment」 - 流程定义备注',
    `ACTIVE`               BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`                VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`             TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`             VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`           DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`           VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`           DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`           VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:w-flow-2
ALTER TABLE W_FLOW
    ADD UNIQUE (`NAME`, `SIGMA`); -- 流程定义名称不重复
ALTER TABLE W_FLOW
    ADD UNIQUE (`CODE`, `SIGMA`); -- 流程定义编码不重复