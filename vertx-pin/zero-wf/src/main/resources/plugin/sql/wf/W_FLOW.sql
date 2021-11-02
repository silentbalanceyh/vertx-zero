-- liquibase formatted sql

-- changeset Lang:w-flow-1
-- 流程定义表：W_FLOW
DROP TABLE IF EXISTS W_FLOW;
CREATE TABLE IF NOT EXISTS W_FLOW
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 流程定义主键',
    `NAMESPACE`     VARCHAR(255) COMMENT '「namespace」- 所在名空间（区分应用的第二维度）',
    `NAME`          VARCHAR(255) COMMENT '「name」- 流程定义名称',
    `CODE`          VARCHAR(255) COMMENT '「code」- 流程定义编号（系统可用）',
    `TYPE`          VARCHAR(36) COMMENT '「type」- 流程类型，BPMN，JBPM等',

    -- 关联图信息
    `GRAPHIC_ID`    VARCHAR(36) UNIQUE COMMENT '「graphicId」- 图关联，1对1',
    `RUN_COMPONENT` TEXT COMMENT '「runComponent」- 执行组件',
    `DATA_XML`      LONGTEXT COMMENT '「dataXml」- 内容的XML格式',
    `DATA_JSON`     LONGTEXT COMMENT '「dataJson」- 内容的JSON格式',
    `DATA_FILE`     VARCHAR(255) COMMENT '「dataFile」- 内容的文件格式',
    `COMMENT`       LONGTEXT COMMENT '「comment」 - 流程定义备注',

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
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:w-flow-2
ALTER TABLE W_FLOW
    ADD UNIQUE (`NAME`, `SIGMA`); -- 流程定义名称不重复
ALTER TABLE W_FLOW
    ADD UNIQUE (`CODE`, `SIGMA`); -- 流程定义编码不重复