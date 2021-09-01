-- liquibase formatted sql

-- changeset Lang:ox-graphic-1
-- 图中专用表：G_GRAPHIC
DROP TABLE IF EXISTS G_GRAPHIC;
CREATE TABLE IF NOT EXISTS G_GRAPHIC
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 图ID',
    `NAME`           VARCHAR(255) COMMENT '「name」- 图名称',
    `CODE`           VARCHAR(36) COMMENT '「code」- neo4j 中的图的 label，符合 neo4j的图ID规范（使用一定命名规则）',
    `MODE`           VARCHAR(20) COMMENT '「mode」- 图模型 FLOW / TOPOLOGY / MIND / TREE',
    `TYPE`           VARCHAR(36) COMMENT '「type」- 图类型 CIRCLE / DEFINE / INSTANCE 圈子、定义、实例，可扩展 tabular',
    `COMMENTS`       TEXT COMMENT '「comments」- 图备注信息',

    -- 图业务信息
    `OWNER_ID`       VARCHAR(36) COMMENT '「ownerId」- 图的拥有者，可管理该图信息的人员ID',
    `UI`             TEXT COMMENT '「ui」- ui配置专用',
    `GRAPHIC_ID`     VARCHAR(36) COMMENT '「graphicId」- 父图ID（当前图是父图的子图，独立管理，创建时需要）',
    `MASTER`         BIT COMMENT '「master」- 主图（不可删除、父ID为NULL、模块级唯一）',

    -- 模块相关 Join
    `MODEL_ID`       VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`      VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `MODEL_CATEGORY` VARCHAR(36) COMMENT '「modelCategory」- 关联的category记录，只包含叶节点',

    -- 特殊字段
    `SIGMA`          VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`       VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`         BIT COMMENT '「active」- 是否启用',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
ALTER TABLE G_GRAPHIC
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;