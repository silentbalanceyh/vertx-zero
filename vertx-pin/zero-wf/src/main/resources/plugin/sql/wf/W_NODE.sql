-- liquibase formatted sql

-- changeset Lang:w-node-1
-- 节点定义表：W_NODE
DROP TABLE IF EXISTS W_NODE;
CREATE TABLE IF NOT EXISTS W_NODE
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 节点定义主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 节点定义名称',
    `CODE`       VARCHAR(255) COMMENT '「code」- 节点定义编号（系统可用）',
    `TYPE`       VARCHAR(36) COMMENT '「type」- 节点类型，START | END | MIDDLE | DECISION',

    -- 关联图/表单 基本信息
    `FLOW_ID`    VARCHAR(36) COMMENT '「flowId」- 所属流程图',

    `FORM_CODE`  VARCHAR(255) COMMENT '「formCode」- 表单代码',
    `FORM_RULE`  TEXT COMMENT '「formRule」- 动态表单规则',

    -- 流程所属
    `MODEL_ID`   VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`  VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',

    -- 主实体（ROLE/GROUP/USER）
    `ENTITY_ID`  VARCHAR(255) COMMENT '「entityId」- 主实体',
    `ENTITY_KEY` VARCHAR(36) COMMENT '「entityKey」- 主实体主键',

    -- 角色/组/用户 规则
    `RULE_ROLE`  TEXT COMMENT '「ruleRole」- 角色规则',
    `RULE_GROUP` TEXT COMMENT '「ruleGroup」- 组规则',
    `RULE_USER`  TEXT COMMENT '「ruleUser」- 用户规则规则',

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
-- changeset Lang:w-node-2
ALTER TABLE W_NODE
    ADD UNIQUE (`CODE`, `SIGMA`); -- 流程定义编码不重复