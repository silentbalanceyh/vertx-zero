-- liquibase formatted sql

-- changeset Lang:w-flow-instance-1
-- 流程定义表：W_FLOW_INSTANCE
DROP TABLE IF EXISTS W_FLOW_INSTANCE;
CREATE TABLE IF NOT EXISTS W_FLOW_INSTANCE

(
    `KEY`        VARCHAR(36) COMMENT '「key」- 流程定义主键',
    `CODE`       VARCHAR(255) COMMENT '「code」- 流程定义编号（系统可用）',
    `SERIAL`     VARCHAR(255) COMMENT '「serial」- 流程实例编号',
    `NAME`       VARCHAR(255) COMMENT '「name」- 流程定义名称',
    `PHASE`      VARCHAR(255) COMMENT '「phase」- 流程实例所属周期',

    -- 关联图信息
    `FLOW_ID`    VARCHAR(36) COMMENT '「flowId」- 所属流程图',
    `USER_ID`    VARCHAR(36) COMMENT '「userId」- 所属人员ID',
    `COORDINATE` LONGTEXT COMMENT '「coordinate」- 所属流程坐标描述',

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
-- changeset Lang:w-flow-instance-2
ALTER TABLE W_FLOW_INSTANCE
    ADD UNIQUE (`CODE`, `SIGMA`); -- 流程定义编码不重复
ALTER TABLE W_FLOW_INSTANCE
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 流程定义序号不重复