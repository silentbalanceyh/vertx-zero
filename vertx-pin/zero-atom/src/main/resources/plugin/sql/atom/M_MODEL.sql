-- liquibase formatted sql

-- changeset Lang:ox-model-1
-- 模型表：M_MODEL
DROP TABLE IF EXISTS M_MODEL;
CREATE TABLE IF NOT EXISTS M_MODEL
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 模型ID',
    -- 模型专用信息
    `IDENTIFIER`       VARCHAR(255) COMMENT '「identifier」- 当前模型全局唯一ID',
    `NAMESPACE`        VARCHAR(255) COMMENT '「namespace」- 当前模型使用的名空间',
    `NAME`             VARCHAR(255) COMMENT '「name」- 当前模型的名称',
    `ALIAS`            VARCHAR(255) COMMENT '「alias」- 模型别名（业务名）',
    `TYPE`             VARCHAR(10) COMMENT '「type」- 当前模型的类型信息',
    `COMMENTS`         TEXT COMMENT '「comments」- 当前模型的描述信息',

    -- 分类相关信息
    `CATEGORY_TREE`    VARCHAR(255) COMMENT '「categoryTree」- 当前模型所属的类型树',
    `CATEGORY_ID`      VARCHAR(36) COMMENT '「categoryId」- 关联的类型的ID',

    -- 标识规则专用字段
    `RULE_UNIQUE`      TEXT COMMENT '「ruleUnique」- 当前模型的标识规则',

    -- 开启特殊功能
    `IS_TRACK`         BIT COMMENT '「isTrack」- 该组件为 track 表示执行 ACTIVITY 的变更监控功能（生成变更历史）',

    -- 基础图定义（蜘蛛）
    `SPIDER`           TEXT COMMENT '「spider」- 主图格式，存储当前模型为起点的图层Json数据，每个模型只有一张',
    `SPIDER_COMPONENT` VARCHAR(255) COMMENT '「spiderComponent」- 在主图格式上存在的插件信息，用于处理主图格式的内容',

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
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-model-2
ALTER TABLE M_MODEL
    ADD UNIQUE (`NAMESPACE`, `IDENTIFIER`) USING BTREE; -- 应用name作为identifier前缀：多应用处理专用
ALTER TABLE M_MODEL
    ADD UNIQUE (`NAMESPACE`, `NAME`) USING BTREE;